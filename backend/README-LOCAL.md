# CluckNet local setup
Install and start Docker Desktop (Linux containers). Open PowerShell in this folder.

```powershell
powershell -ExecutionPolicy Bypass -File .\setup-local.ps1
docker compose config --quiet
docker compose up -d --build
docker compose ps
docker compose logs -f backend
```

Swagger: http://localhost:8080/swagger-ui/index.html . InfluxDB: http://localhost:8086 (admin and INFLUXDB_PASSWORD in .env). MySQL: `docker compose exec mysql mysql -u cluckuser -p clucknet` then `SHOW TABLES;`.

MQTT smoke test: `docker compose exec mosquitto mosquitto_pub -h localhost -t clucknet/zones/1/telemetry -m '{"zoneId":1,"deviceId":"test-device","temperature":28.5,"humidity":65.0,"nh3":5.0,"lpg":0.0}'` (PowerShell native argument quoting varies; use MQTTX if needed). Create a valid zone and use its actual ID and a compatible device ID before expecting telemetry persistence. Inspect backend logs. Firebase push notifications are unavailable without Firebase credentials.

Stop with `docker compose down`; do not add `-v` unless intentionally deleting all database data. Keep .env unchanged across restarts. MQTT is anonymous and loopback-only for local development; backend port 8080 is exposed for mobile testing. Use trusted networks only. For Android emulator use http://10.0.2.2:8080; physical phone use PC LAN IP and allow port 8080 on private firewall profile.

AWS later: replace MQTT endpoint with your own AWS IoT endpoint, enable TLS and mount PKCS12/JKS stores read-only, set AWS_IOT_KEYSTORE_PATH, AWS_IOT_KEYSTORE_PASSWORD, AWS_IOT_TRUSTSTORE_PATH and AWS_IOT_TRUSTSTORE_PASSWORD. Use managed secrets and private databases. No AWS resources are required locally.

Review limitations: six missing DTO classes reconstructed from callers, stale User import removed, spring-messaging dependency added. DTO contracts need comparison with original frontend/firmware. No Docker or Maven executable available in review environment; build and integration tests have not been run. Original generated target and IDE files omitted. Rotate any previously exposed secrets.
