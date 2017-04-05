CREATE TABLE ocupacion(
	id_ocupacion INTEGER NOT NULL IDENTITY,
	descripcion VARCHAR(50) NOT NULL,
	id_tipoocupacion INTEGER NOT NULL
);

CREATE TABLE direccion(
	id_direccion INTEGER NOT NULL IDENTITY,
	calle VARCHAR(50) NOT NULL,
	numero INTEGER NOT NULL
);

CREATE TABLE persona(
	id_persona INTEGER NOT NULL IDENTITY,
	nombre VARCHAR(50) NOT NULL,
	id_direccion INTEGER NOT NULL,
	id_ocupacion INTEGER NOT NULL
);

CREATE TABLE tipo_ocupacion(
	id_tipoocupacion INTEGER NOT NULL IDENTITY,
	descripcion VARCHAR(50) NOT NULL
);

