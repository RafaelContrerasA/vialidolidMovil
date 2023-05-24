<?php
if ($_SERVER["REQUEST_METHOD"] == "GET") {
    require_once 'conexion.php';
    $id_ciudadano = $_GET['id_ciudadano'];
    $tipo_reporte = $_GET['tipo_reporte']; // Nuevo campo agregado
    $query = "SELECT fecha, descripcion, latitud, longitud, estatus
    FROM reporte 
    WHERE id_ciudadano = '".$id_ciudadano."' AND tipo_reporte = '".$tipo_reporte."'";
    $resultado = $mysql->query($query);
    
    if ($resultado->num_rows > 0) {
        $array = array();
        while ($row = $resultado->fetch_assoc()) {
            $array[] = $row;
        }
        echo json_encode($array);
    } else {
        echo "No hay registros";
    }
    
    $resultado->close();
    $mysql->close();
}
?>