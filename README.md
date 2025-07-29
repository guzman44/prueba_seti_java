# prueba_seti_java


choco -v

Set-ExecutionPolicy Bypass -Scope Process -Force; `
[System.Net.ServicePointManager]::SecurityProtocol = `
[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; `
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

Esto instalará choco (puedes cerrar y volver a abrir PowerShell luego).

choco install terraform -y

terraform -version






seti.franquicia/
├── pom.xml
├── Dockerfile
├── terraform/
│   └── main.tf
├──src/
	└── main/
		├── java/com/nequi/franquicia/
		│   ├── applications/
		│   ├── domain/
		│   │   ├── model/
		│   │   └── usecase/
		│   ├── infrastructure/
		│   │   ├── entry-points/
		│   │   └── driven-adapters/
		│
		└── resources/
			├── application.yml
			└── schema.sql (Migracion Flyway)