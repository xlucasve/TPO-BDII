Use ECommerce
GO

CREATE TABLE [facturas] (
  [factura_id] integer IDENTITY(1,1) PRIMARY KEY,
  [operador_responsable] integer,
  [cliente] integer,
  [pago_pedido] integer
)
GO

CREATE TABLE [pagos] (
  [pago_id] integer IDENTITY(1,1) PRIMARY KEY,
  [fecha_pago] datetime,
  [metodo_pago] varchar(255),
  [monto] integer
)
GO

CREATE TABLE [clientes] (
  [cliente_id] integer IDENTITY(1,1) PRIMARY KEY,
  [cliente_id_sistema] varchar(255),
  [nombre] varchar(255),
  [apellido] varchar(255),
  [direccion] varchar(255),
  [dni] integer
)
GO

CREATE TABLE [operadores] (
  [operador_id] integer IDENTITY(1,1)PRIMARY KEY,
  [nombre] varchar(255),
  [apellido] varchar(255),
  [dni] integer
)
GO

ALTER TABLE [facturas] ADD FOREIGN KEY ([operador_responsable]) REFERENCES [operadores] ([operador_id])
GO

ALTER TABLE [facturas] ADD FOREIGN KEY ([pago_pedido]) REFERENCES [pagos] ([pago_id])
GO

ALTER TABLE [facturas] ADD FOREIGN KEY ([cliente]) REFERENCES [clientes] ([cliente_id])
GO
