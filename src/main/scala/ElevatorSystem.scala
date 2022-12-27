import java.lang.Math.abs

object ElevatorSystem {
  private var elevators: List[Elevator] = List.empty
  private var numberOfFloors: Int = 0
  def apply(numberOfFloorsInBuilding: Int, initialFloorsWithElevators: List[Int]): Unit = {
    require(numberOfFloorsInBuilding > 0, "Liczba pięter musi być liczbą dodatnią!")
    numberOfFloors = numberOfFloorsInBuilding
    require(initialFloorsWithElevators.forall(floor => floor <= numberOfFloors && floor >= 0),
      "Numery pięter nie mogą być liczbami ujemnymi i nie mogą być większe od ilości pięter w budynku!")
    elevators = initialFloorsWithElevators.zipWithIndex.map{ case(floor, index) => Elevator(index, floor, None)}
  }

  def pickup(pickupFloor: Int, destination: Int): Unit = {
    val elevator = findNearestElevator(pickupFloor)
    update(Elevator(elevator.id, elevator.currentFloor, Some(pickupFloor)))
    steps()
    update(Elevator(elevator.id, pickupFloor, Some(destination)))
  }

  private def findNearestElevator(floor: Int): Elevator = {
    def loop(best: Elevator, rest: List[Elevator]): Elevator = {
      rest match {
        case Nil => best
        case x::xs => {
          if (abs(floor - x.currentFloor) < abs(floor - best.currentFloor))
            loop(x, xs)
          else
            loop(best, xs)
        }
      }
    }
    loop(elevators.head, elevators.tail)
  }

  private def update(elevator: Elevator): Unit = {
    elevators = elevators.updated(elevator.id, elevator)
  }

  private def printUpdateInfo(id: Int, currentFloor: Int, destination: Int): Unit = {
    val direction = if(destination > currentFloor) "górę" else "dół"
    println(s"Winda $id porusza się z piętra $currentFloor w $direction na piętro $destination.")
  }

  private def step(): Unit = {
    elevators.filterNot { e => e.destinationFloor.isEmpty }
      .foreach { case e@Elevator(id, currentFloor, Some(destinationFloor)) => {
        if (destinationFloor == currentFloor)
          update(e.copy(destinationFloor = None))
        else if (destinationFloor > currentFloor) {
          printUpdateInfo(id, currentFloor, currentFloor + 1)
          update(e.copy(currentFloor = currentFloor + 1))
        }
        else {
          printUpdateInfo(id, currentFloor, currentFloor - 1)
          update(e.copy(currentFloor = currentFloor - 1))
        }
      }
      }
  }

  def steps(): Unit = {
    def loop(): Unit = {
      if (elevators.exists(e => e.destinationFloor.nonEmpty)) {
        step()
        loop()
      }
    }
    loop()
  }

  def getElevators: List[Elevator] = elevators

  def getNumberOfFloors: Int = numberOfFloors

  def getElevator(i: Int): Elevator = elevators(i)

  private def setElevators(newElevators: List[Elevator]): Unit = elevators = newElevators

}



