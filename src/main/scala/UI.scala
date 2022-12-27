import scala.io.StdIn.readLine

object UI {
  def readInt(): Int = {
    try {
      readLine().toInt
    } catch {
      case _: Throwable => {
        println("Niepoprawn dane. Wprowadź liczbę.")
        readInt()
      }
    }
  }

  def readList(): List[Int] = {
    try {
      readLine().split(" ").toList.map(_.toInt)
    } catch {
      case _: Throwable => {
        println("Niepoprawne dane. Wprowadź liczby oddzielone spacjami.")
        readList()
      }
    }
  }

  def printFloorList(message: String):Unit = {
    println(message)
    (0 to ElevatorSystem.getNumberOfFloors).foreach{floor => println(s"Piętro $floor: wybierz $floor")}
  }

  def printElevators(): Unit = {
    ElevatorSystem.getElevators
      .foreach(e => println(s"Winda ${e.id} znajduje się na piętrze ${e.currentFloor}"))
  }

  def end(): Boolean = {
    println("Zakonczyć działanie programu?")
    println("Tak: Wybierz T")
    println("Nie: Wybierz N")
    val input = readLine().toUpperCase()
    input match {
      case "T" => true
      case "F" => false
      case _ =>
        println("Niepoprawne dane. Wprowadź jedną literę, T lub N.")
        end()
    }
  }
}
