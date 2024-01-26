
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.Assertions._
import Operation.BusquedaBinaria

class BusquedaBinariaTest extends AnyFunSuite {

  test("busquedaBinaria should find the correct position of an element in the sorted list") {
    val lista = List(5, 8, 12, 15, 22, 30, 34, 40)

    assert(BusquedaBinaria.busquedaBinaria(lista, 22) === 4)
  }

  test("busquedaBinaria should return -1 for an element not present in the list") {
    val lista = List(5, 8, 12, 15, 22, 30, 34, 40)

    assert(BusquedaBinaria.busquedaBinaria(lista, 25) === -1)
  }

  test("busquedaBinaria should handle an empty list") {
    val lista = List.empty[Int]

    assert(BusquedaBinaria.busquedaBinaria(lista, 10) === -1)
  }
}

