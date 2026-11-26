# CluckNet infrastructure
Fresh deployment adapted from supplied Shopverse configuration. Do not use against an existing Shopverse state: names and node group key changed. Existing VPC/EKS modules and EBS CSI Pod Identity configuration retained. No ECR, ALB controller or IoT resources added. Existing subnet ELB tags retained for later use.

## Deploy (PowerShell)
Use a new state directory. Copy terraform.tfvars.example to terraform.tfvars and replace the example IP with your public IP /32. Original public API default is open to all IPs; override it before applying.

```
aws sts get-caller-identity --profile terraform-user
terraform init
terraform fmt -recursive
terraform validate
terraform plan -out=clucknet.tfplan
terraform apply clucknet.tfplan
terraform output
aws eks update-kubeconfig --region ap-south-1 --name clucknet-eks --profile terraform-user
kubectl get nodes
kubectl get pods -n kube-system
```
Verify instance class, engine version and EKS version availability in Mumbai. Terraform was not executed in the generation environment.

## RDS
Private subnet group spans existing two AZs; ingress TCP 3306 only from EKS node security group (standard VPC CNI networking). RDS manages the master password in Secrets Manager; retrieve it through the console with authorized access. This creates database clucknet and master user cluckadmin, not application user cluckuser. Connect from an authorized MySQL client inside EKS, using TLS and the RDS CA bundle. Run SQL interactively, replacing the placeholder with a strong password:

```sql
CREATE USER 'cluckuser'@'%' IDENTIFIED BY 'REPLACE_WITH_STRONG_PASSWORD' REQUIRE SSL;
GRANT ALL PRIVILEGES ON clucknet.* TO 'cluckuser'@'%';
```
Use terraform output -raw rds_jdbc_url in backend ConfigMap DATABASE_URL, and cluckuser/password in Kubernetes Secret. Configure Java truststore with RDS CA for VERIFY_IDENTITY. Hibernate update can create tables, not migrate existing data. No database password is hardcoded in Terraform. RDS managed master secret incurs charges.

## Cleanup
Back up data first. Delete Kubernetes PVCs and any retained EBS volumes only when no longer needed. Remove any separately created load balancers before VPC destruction. RDS deletion protection defaults to true; explicitly set rds_deletion_protection=false and apply before destroying. Final snapshot defaults to enabled; if a snapshot with the configured name already exists, change the identifier before destruction. For disposable data only, set rds_skip_final_snapshot=true and apply. Then terraform destroy. Snapshots and retained EBS volumes can continue billing. EKS, nodes, NAT and RDS incur charges while idle.
