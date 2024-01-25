
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import contable.Camion
import java.nio.file.Paths

class CamionSpec extends AnyFlatSpec with Matchers {
  "Camion.costo" should "calcular el costo total correctamente" in {
    val nombreArchivo = Paths.get("./src/resources/camion.csv") // Cambia la ruta según tu estructura
    val costo = Camion.costo(nombreArchivo)
    costo shouldEqual 72037.15 // Ajusta este valor según el contenido de tu archivo de prueba
  }
}