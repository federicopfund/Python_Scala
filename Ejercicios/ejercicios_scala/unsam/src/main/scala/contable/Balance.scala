package varios

import scala.io.Source
import java.nio.file.{Path, Paths}

object ProcesadorCamion {

  def leerCamion(pathCamion: Path): List[Map[String, Any]] = {
    try {
      val lines = Source.fromFile(nuevoArchivo).getLines().toList
      val headers = lines.head.split(",")
      val camion = lines.tail.map { line =>
        val fields = line.split(",")
        Map(headers.zip(fields): _*)
      }
      camion
    } catch {
      case _: Throwable =>
        println("No corresponde a una dirección válida.")
        List.empty
    }
  }

  def totalPagado(precioCompra: List[Map[String, Any]]): Double = {
    precioCompra.map(s => s("cajones").asInstanceOf[Int] * s("precio").asInstanceOf[Double]).sum
  }

  def leerPrecios(pathPrecio: Path): Map[String, Double] = {
    try {
      val lines = Source.fromFile(nuevoArchivo).getLines().toList
      lines.flatMap { line =>
        val fields = line.split(",")
        if (fields.length == 2) Some(fields(0) -> fields(1).toDouble) else None
      }.toMap
    } catch {
      case _: Throwable => Map.empty
    }
  }

  def diferencia(precioCompra: List[Map[String, Any]], precioVenta: Map[String, Double]): Double = {
    precioCompra.flatMap { s =>
      precioVenta.get(s("nombre").asInstanceOf[String]).map { precio =>
        val venta = precio * s("cajones").asInstanceOf[Int]
        val compra = s("cajones").asInstanceOf[Int] * s("precio").asInstanceOf[Double]
        venta - compra
      }
    }.sum
  }

  def recauda(precioCompra: List[Map[String, Any]], precioVenta: Map[String, Double]): Double = {
    precioCompra.flatMap { s =>
      precioVenta.get(s("nombre").asInstanceOf[String]).map { precio =>
        precio * s("cajones").asInstanceOf[Int]
      }
    }.sum
  }

  def line(): Unit = {
    println("-" * (14 * 7))
  }

  def mostrarPantalla(pathCamion: Path, pathPrecio: Path): Unit = {
    val precioCompra = leerCamion(pathCamion)
    val totalPagadoEnCompra = totalPagado(precioCompra)
    val precioVenta = leerPrecios(pathPrecio)
    val diferenciaEnVentas = diferencia(precioCompra, precioVenta)
    val recaudacionDeVentas = recauda(precioCompra, precioVenta)

    line()
    println("| nombre   | precio_venta  | diferencia_de_venta | recaudacion_de_venta | total_pagado_en_compra |")
    line()
    precioVenta.keys.foreach { e =>
      println(f"|${e.padTo(9, ' ')} | ${precioVenta(e).toString.padTo(14, ' ')}| ${diferenciaEnVentas.toString.padTo(19, ' ')}  | ${recaudacionDeVentas.toString.padTo(22, ' ')} | ${totalPagadoEnCompra.toString.padTo(22, ' ')} |")
    }
    line()
  }

  def main(args: Array[String]): Unit = {
    mostrarPantalla(pathCamion,pathPrecio)
  }
}
