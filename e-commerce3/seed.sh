#!/bin/bash

BASE_URL="http://localhost:8080"

# ─────────────────────────────────────────────
# USUARIO ADMIN
# ─────────────────────────────────────────────
echo "=== Registrando usuario admin ==="
curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Admin",
    "apellido": "Sistema",
    "email": "admin@ecommerce.com",
    "telefono": "1122334455",
    "direccion": "Av. Corrientes 1234, CABA",
    "rol": "ADMIN",
    "password": "admin123"
  }' | cat

echo ""
echo "=== Login ==="
TOKEN=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@ecommerce.com", "password": "admin123"}' \
  | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
  echo "ERROR: No se pudo obtener el token. Verificar que el servidor este corriendo."
  exit 1
fi

echo "Token obtenido OK"
AUTH="Authorization: Bearer $TOKEN"

# ─────────────────────────────────────────────
# HELPERS
# ─────────────────────────────────────────────
create_categoria() {
  NAME=$1
  ID=$(curl -s -X POST "$BASE_URL/api/categorias" \
    -H "Content-Type: application/json" \
    -H "$AUTH" \
    -d "{\"nombre\": \"$NAME\"}" \
    | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
  echo "  Categoria '$NAME' creada (id $ID)" >&2
  echo "$ID"
}

create_producto() {
  NOMBRE="$1"
  DESC="$2"
  PRECIO=$3
  STOCK=$4
  CATS="$5"

  RESP=$(curl -s -X POST "$BASE_URL/api/productos" \
    -H "Content-Type: application/json" \
    -H "$AUTH" \
    -d "{\"nombre\": \"$NOMBRE\", \"descripcion\": \"$DESC\", \"precio\": $PRECIO, \"stock\": $STOCK, \"categoriaIds\": [$CATS]}")

  ID_PROD=$(echo "$RESP" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
  if [ -n "$ID_PROD" ]; then
    echo "  OK '$NOMBRE' (id $ID_PROD)"
  else
    echo "  ERROR '$NOMBRE': $RESP"
  fi
}

# ─────────────────────────────────────────────
# CATEGORIAS
# ─────────────────────────────────────────────
echo ""
echo "=== Creando categorias ==="

CAT_ELEC=$(create_categoria "Electronica")
CAT_ROPA=$(create_categoria "Ropa")
CAT_HOGAR=$(create_categoria "Hogar")
CAT_DEP=$(create_categoria "Deportes")
CAT_LIB=$(create_categoria "Libros")
CAT_JUG=$(create_categoria "Juguetes")
CAT_BEL=$(create_categoria "Belleza y Cuidado Personal")
CAT_ALI=$(create_categoria "Alimentos y Bebidas")
CAT_HER=$(create_categoria "Herramientas")
CAT_MAS=$(create_categoria "Mascotas")

# ─────────────────────────────────────────────
# PRODUCTOS
# ─────────────────────────────────────────────
echo ""
echo "=== Creando productos ==="

# Electronica
create_producto \
  "iPhone 15 Pro 256GB" \
  "Smartphone Apple con chip A17 Pro, pantalla Super Retina XDR de 6.1 pulgadas y camara triple de 48 MP. Incluye Dynamic Island y conector USB-C." \
  1599999 15 "$CAT_ELEC"

create_producto \
  "Samsung Galaxy S24 128GB" \
  "Smartphone Samsung con pantalla Dynamic AMOLED de 6.2 pulgadas, procesador Snapdragon 8 Gen 3 y bateria de 4000 mAh. Resistente al agua IP68." \
  1299999 20 "$CAT_ELEC"

create_producto \
  "MacBook Air M3 13 pulgadas" \
  "Laptop ultradelgada de Apple con chip M3, pantalla Liquid Retina de 13 pulgadas, 8 GB de RAM unificada y 256 GB SSD. Hasta 18 horas de autonomia." \
  2499999 8 "$CAT_ELEC"

create_producto \
  "Auriculares Sony WH-1000XM5" \
  "Auriculares inalambricos over-ear con cancelacion de ruido activa, 30 horas de bateria y audio de alta resolucion. Ideales para viajes y home office." \
  89999 35 "$CAT_ELEC"

create_producto \
  "Smart TV Samsung 55 pulgadas 4K" \
  "Televisor 4K UHD con procesador Crystal 4K, HDR10+ y sistema operativo Tizen. Compatible con Alexa y Google Assistant. Pantalla sin marcos." \
  799999 12 "$CAT_ELEC"

create_producto \
  "Tablet iPad 10ma generacion" \
  "Tablet Apple con chip A14 Bionic, pantalla Liquid Retina de 10.9 pulgadas, 64 GB de almacenamiento y conectividad Wi-Fi 6. Compatible con Apple Pencil." \
  699999 18 "$CAT_ELEC"

# Ropa
create_producto \
  "Remera algodon basica blanca" \
  "Remera de algodon 100% peinado, corte recto unisex. Suave al tacto y resistente a lavados frecuentes. Disponible en talles S a XL." \
  4999 100 "$CAT_ROPA"

create_producto \
  "Jean slim fit elastizado azul" \
  "Jean con 2% de elastano para maxima comodidad de movimiento. Corte slim de tiro medio con bolsillos traseros decorativos. Talla M." \
  9999 60 "$CAT_ROPA"

create_producto \
  "Campera de cuero negro talle M" \
  "Campera de cuero genuino con cierre metalico frontal, bolsillos laterales con cierre y forro interior de polar. Acabado mate premium." \
  24999 25 "$CAT_ROPA"

create_producto \
  "Zapatillas Nike Air Max Running" \
  "Zapatillas con tecnologia Air Max en el talon, suela de goma vulcanizada y parte superior de mesh transpirable. Aptas para running y uso diario." \
  19999 40 "$CAT_ROPA,$CAT_DEP"

create_producto \
  "Buzo hoodie french terry gris" \
  "Buzo con capucha ajustable y bolsillo canguro frontal. Tela french terry de algodon con puños elastizados. Corte unisex, talle unico." \
  7999 80 "$CAT_ROPA"

# Hogar
create_producto \
  "Silla ergonomica de oficina pro" \
  "Silla con soporte lumbar ajustable, apoyabrazos regulables, asiento de espuma de alta densidad y base giratoria con ruedas de goma. Capacidad 120 kg." \
  49999 18 "$CAT_HOGAR"

create_producto \
  "Set de cuchillos de cocina 5 piezas" \
  "Juego de 5 cuchillos de acero inoxidable con mangos ergonomicos de madera. Incluye cuchillo chef, pan, verduras, deshuesador y mondador. Con soporte." \
  12999 30 "$CAT_HOGAR"

create_producto \
  "Licuadora de alta potencia 1000W" \
  "Licuadora con motor de 1000W, vaso de vidrio templado de 2 litros, 5 velocidades y funcion pulso. Facil de limpiar y silenciosa. Garantia 2 anios." \
  8999 45 "$CAT_HOGAR"

create_producto \
  "Aspiradora robot con mapeo WiFi" \
  "Aspiradora robotica con mapeo laser inteligente, control por app, autonomia de 120 minutos y regreso automatico a la base de carga. Compatible con Alexa." \
  129999 10 "$CAT_HOGAR"

create_producto \
  "Juego de sabanas 2 plazas percal" \
  "Juego de 3 piezas en percal 200 hilos. Incluye sabana bajera con elastico ajustable, sabana encimera y 2 fundas de almohada. Lavable a maquina 40 C." \
  5999 50 "$CAT_HOGAR"

# Deportes
create_producto \
  "Pelota de futbol oficial Nike" \
  "Pelota reglamentaria tamano 5 con 32 paneles cosidos a mano y camara de butilo para mayor retencion de aire. Apta para cesped natural y artificial." \
  3999 55 "$CAT_DEP"

create_producto \
  "Mancuernas ajustables 20 kg par" \
  "Par de mancuernas ajustables de 2 a 20 kg por unidad con discos de hierro recubierto y sistema de seguridad de tornillo. Incluye 4 discos de 2.5 kg." \
  18999 22 "$CAT_DEP"

create_producto \
  "Bicicleta de montana rodado 29" \
  "Mountain bike con marco de aluminio 6061, suspension delantera de 100 mm, frenos hidraulicos de disco y 21 velocidades Shimano. Peso 13.5 kg." \
  249999 6 "$CAT_DEP"

create_producto \
  "Colchoneta yoga antideslizante 6mm" \
  "Mat de yoga de 6 mm de espesor con superficie antideslizante en ambas caras. Fabricada en NBR ecologico, libre de ftalatos. Incluye correa de transporte." \
  3499 70 "$CAT_DEP"

create_producto \
  "Caminadora electrica plegable 2.5HP" \
  "Cinta de correr plegable con motor de 2.5 HP, velocidad de 1 a 14 km/h, pantalla LCD con metricas de entrenamiento y 12 programas preinstalados." \
  199999 5 "$CAT_DEP,$CAT_HOGAR"

# Libros
create_producto \
  "El Senor de los Anillos edicion completa" \
  "Trilogia completa de J.R.R. Tolkien en un solo volumen de tapa dura. Incluye apendices, mapas de la Tierra Media y prologo del autor. Edicion especial." \
  6999 40 "$CAT_LIB"

create_producto \
  "Cien anios de soledad edicion conmemorativa" \
  "Obra maestra del realismo magico de Gabriel Garcia Marquez. Edicion especial con prologo de la RAE, fotografias del proceso creativo y notas al pie." \
  4999 35 "$CAT_LIB"

create_producto \
  "Clean Code Robert C. Martin" \
  "Guia de referencia sobre escritura de codigo limpio y mantenible por Robert C. Martin. Con ejemplos practicos en Java y principios SOLID explicados paso a paso." \
  5499 28 "$CAT_LIB"

create_producto \
  "Harry Potter y la piedra filosofal coleccionista" \
  "Primera entrega de la saga de J.K. Rowling en edicion de coleccionista con ilustraciones de MinaLima, tapa dura y paginas con bordes dorados." \
  8999 20 "$CAT_LIB"

# Mascotas
create_producto \
  "Croquetas premium perros adultos 15kg" \
  "Alimento balanceado para perros adultos con pollo y arroz como primer ingrediente. Sin colorantes ni conservantes artificiales. Enriquecido con Omega-3." \
  12999 30 "$CAT_MAS"

create_producto \
  "Torre rascadora para gatos 120cm" \
  "Rascador de sisal de 120 cm con 3 plataformas, cueva interior acolchada y juguete colgante. Base ancha estable con alfombra antideslizante." \
  9999 15 "$CAT_MAS"

create_producto \
  "Acuario de vidrio templado 80 litros" \
  "Pecera rectangular de vidrio templado de 80 litros con tapa, iluminacion LED regulable, filtro interno silencioso y termometro digital. Lista para armar." \
  18999 8 "$CAT_MAS"

create_producto \
  "Arnes acolchado reflectivo para perros" \
  "Arnes de tela acolchada con broches de seguridad, franjas reflectivas para visibilidad nocturna y argolla dorsal. Talle M, perros de 10 a 20 kg." \
  2999 50 "$CAT_MAS"

echo ""
echo "=== Seed completado ==="
