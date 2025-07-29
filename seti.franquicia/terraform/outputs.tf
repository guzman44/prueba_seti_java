output "rds_endpoint" {
  value = aws_db_instance.mysql_franquicia.endpoint
}

output "database_identifier" {
  value = aws_db_instance.mysql_franquicia.id
}