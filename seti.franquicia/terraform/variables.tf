variable "region" {
  type    = string
  default = "us-east-2"
}

variable "vpc_id" {
  type = string
}

variable "subnet_ids" {
  type = list(string)
}

variable "db_username" {
  type    = string
  default = "root"
}

variable "db_password" {
  type    = string
  sensitive = true
}