provider "aws" {
  region = var.region
}

resource "aws_db_subnet_group" "franquicia_subnet_group" {
  name       = "franquicia-subnet-group"
  subnet_ids = var.subnet_ids
}

resource "aws_security_group" "franquicia_sg" {
  name        = "franquicia-sg"
  description = "SG for RDS MySQL franquicia"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_instance" "mysql_franquicia" {
  identifier              = "mysql-franquicia"
  allocated_storage       = 20
  storage_type            = "gp2"
  engine                  = "mysql"
  engine_version          = "8.0"
  instance_class          = "db.t3.micro"
  db_name                 = "franquicias_db"
  username                = var.db_username
  password                = var.db_password
  db_subnet_group_name    = aws_db_subnet_group.franquicia_subnet_group.name
  vpc_security_group_ids  = [aws_security_group.franquicia_sg.id]
  publicly_accessible     = true
  skip_final_snapshot     = true
  deletion_protection     = false
}