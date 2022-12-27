object Main extends App{
  print("Wprowadź liczbę pięter w budynku: ")
  val numberOfFloors = UI.readInt()
  print("Wprowadź po spacji numery pięter na których znajdują się windy: ")
  val initialFloorsWithElevators = UI.readList()
  ElevatorSystem(numberOfFloors , initialFloorsWithElevators)
  run()

  def run(): Unit = {
    UI.printElevators()
    UI.printFloorList("Wybierz piętro na którym się znajdujesz.")
    val currentFloor = UI.readInt()

    UI.printFloorList("Wbierz piętro na które chcesz pojechać")
    val destinationFloor = UI.readInt()

    ElevatorSystem.pickup(currentFloor, destinationFloor)

    ElevatorSystem.steps()
    UI.printElevators()
    val end: Boolean = UI.end()
    if (!end) run()
  }
}
