resource "aws_db_subnet_group" "mysql" {
  name       = "${var.project_name}-db-subnets"
  subnet_ids = module.vpc.private_subnets
  tags       = { Project = var.project_name, Environment = var.environment }
}

resource "aws_security_group" "mysql" {
  name        = "${var.project_name}-rds-sg"
  description = "MySQL access from EKS worker nodes"
  vpc_id      = module.vpc.vpc_id
  tags        = { Project = var.project_name, Environment = var.environment }
}

resource "aws_vpc_security_group_ingress_rule" "mysql_from_eks" {
  security_group_id            = aws_security_group.mysql.id
  referenced_security_group_id = module.eks.node_security_group_id
  ip_protocol                  = "tcp"
  from_port                    = 3306
  to_port                      = 3306
}

resource "aws_db_instance" "mysql" {
  identifier                  = "${var.project_name}-mysql"
  engine                      = "mysql"
  engine_version              = "8.0"
  instance_class              = var.rds_instance_class
  allocated_storage           = 20
  storage_type                = "gp3"
  storage_encrypted           = true
  db_name                     = "clucknet"
  username                    = "cluckadmin"
  manage_master_user_password = true
  port                        = 3306
  db_subnet_group_name        = aws_db_subnet_group.mysql.name
  vpc_security_group_ids      = [aws_security_group.mysql.id]
  publicly_accessible         = false
  multi_az                    = false
  backup_retention_period     = 1
  auto_minor_version_upgrade  = true
  deletion_protection         = var.rds_deletion_protection
  skip_final_snapshot         = var.rds_skip_final_snapshot
  final_snapshot_identifier   = var.rds_skip_final_snapshot ? null : "${var.project_name}-mysql-final"
  tags                        = { Project = var.project_name, Environment = var.environment }
}
