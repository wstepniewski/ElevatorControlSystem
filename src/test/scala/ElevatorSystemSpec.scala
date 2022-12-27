import org.scalatest.PrivateMethodTester
import org.scalatest.funspec.AnyFunSpec


class ElevatorSystemSpec extends AnyFunSpec with PrivateMethodTester{
  describe("Elevator System constructor") {
    it("should throw IllegalArgumentException when creating ElevatorSystem with negative number of floors") {
      assertThrows[IllegalArgumentException](ElevatorSystem(-10, List.empty))
    }
    it("should throw IllegalArgumentException when creating ElevatorSystem with 0 floors") {
      assertThrows[IllegalArgumentException](ElevatorSystem(0, List.empty))
    }
    it("should throw IllegalArgumentException when creating ElevatorSystem with elevators on negative floors") {
      assertThrows[IllegalArgumentException](ElevatorSystem(10, List(-10)))
    }
    it("should throw IllegalArgumentException when creating ElevatorSystem with elevators on floors greater than number of floors in building") {
      assertThrows[IllegalArgumentException](ElevatorSystem(10, List(11)))
    }
    it("should successfully create ElevatorSystem"){
      val numberOfFloors = 10
      val initialFloorsWithElevators = List(0, 1, 5, 10)
      val elevators = createTestElevators(initialFloorsWithElevators, List.fill(4)(None))
      ElevatorSystem(numberOfFloors, initialFloorsWithElevators)
      assert(numberOfFloors == ElevatorSystem.getNumberOfFloors)
      assert(elevators == ElevatorSystem.getElevators)
    }
  }

  describe("Find Nearest Elevator") {
    val findNearestElevatorPrivate = PrivateMethod[Elevator](Symbol("findNearestElevator"))

    it("should return elevator on the same floor"){
      ElevatorSystem(8, List(1, 2, 2, 3, 5, 8))
      val actual = ElevatorSystem invokePrivate(findNearestElevatorPrivate(5))
      val expected = ElevatorSystem.getElevator(4)
      assert(actual == expected)
    }
    it("should return elevator on lower floor when lower and upper are in the same distance"){
      ElevatorSystem(8, List(1, 2, 2, 3, 5, 8))
      val actual = ElevatorSystem invokePrivate findNearestElevatorPrivate(4)

      val expected = ElevatorSystem.getElevator(3)
      assert(actual == expected)
    }
    it("should return first elevator on this floor"){
      ElevatorSystem(8, List(1, 2, 2, 3, 5, 8))
      val actual = ElevatorSystem invokePrivate findNearestElevatorPrivate(2)

      val expected = ElevatorSystem.getElevator(1)
      assert(actual == expected)
    }
    it("should return elevator on lower floor when it is the nearest one"){
      ElevatorSystem(8, List(1, 2, 2, 3, 5, 8))
      val actual = ElevatorSystem invokePrivate findNearestElevatorPrivate(6)

      val expected = ElevatorSystem.getElevator(4)
      assert(actual == expected)
    }
    it("should return elevator on upper floor when it is the nearest one") {
      ElevatorSystem(8, List(1, 2, 2, 3, 5, 8))
      val actual = ElevatorSystem invokePrivate findNearestElevatorPrivate(7)

      val expected = ElevatorSystem.getElevator(5)
      assert(actual == expected)
    }
  }

  describe("Step"){
    it("should correctly run successive steps"){
      val setElevatorsPrivate = PrivateMethod[Elevator](Symbol("setElevators"))

      val initialFloors: List[Int] = List(1, 3, 5, 8, 10)
      val initialDestinations: List[Option[Int]] = List(None, Some(4), Some(3), Some(7), Some(10))
      val numberOfFloors = 10
      ElevatorSystem(numberOfFloors, initialFloors)
      val elevatorsInMovement = createTestElevators(initialFloors, initialDestinations)

      ElevatorSystem invokePrivate setElevatorsPrivate(elevatorsInMovement)
      val stepPrivate = PrivateMethod[Unit](Symbol("step"))
      ElevatorSystem invokePrivate stepPrivate()

      val floorsIt1 = List(1, 4, 4, 7, 10)
      val destinationsIt1 = List(None, Some(4), Some(3), Some(7), None)
      val actualIt1 = ElevatorSystem.getElevators
      val expectedIt1 = createTestElevators(floorsIt1, destinationsIt1)
      assert(actualIt1 == expectedIt1)

      ElevatorSystem invokePrivate setElevatorsPrivate(expectedIt1)
      ElevatorSystem invokePrivate stepPrivate()

      val floorsIt2 = List(1, 4, 3, 7, 10)
      val destinationsIt2 = List(None, None, Some(3), None, None)
      val actualIt2 = ElevatorSystem.getElevators
      val expectedIt2 = createTestElevators(floorsIt2, destinationsIt2)
      assert(actualIt2 == expectedIt2)

      ElevatorSystem invokePrivate setElevatorsPrivate(expectedIt2)
      ElevatorSystem invokePrivate stepPrivate()

      val floorsIt3 = floorsIt2
      val destinationsIt3 = List(None, None, None, None, None)
      val actualIt3 = ElevatorSystem.getElevators
      val expectedIt3 = createTestElevators(floorsIt3, destinationsIt3)
      println(actualIt3)
      println(expectedIt3)
      assert(actualIt3 == expectedIt3)
    }
  }

  describe("Steps"){
    it("give same results as test above"){
      val setElevatorsPrivate = PrivateMethod[Elevator](Symbol("setElevators"))

      val initialFloors: List[Int] = List(1, 3, 5, 8, 10)
      val initialDestinations: List[Option[Int]] = List(None, Some(4), Some(3), Some(7), Some(10))
      val numberOfFloors = 10
      ElevatorSystem(numberOfFloors, initialFloors)
      val elevatorsInMovement = createTestElevators(initialFloors, initialDestinations)
      ElevatorSystem invokePrivate setElevatorsPrivate(elevatorsInMovement)

      ElevatorSystem.steps()

      val expectedFloors = List(1, 4, 3, 7, 10)
      val expectedDestinations = List(None, None, None, None, None)
      val expected = createTestElevators(expectedFloors, expectedDestinations)
      val actual = ElevatorSystem.getElevators

      assert(actual == expected)
    }
  }

  describe("Pickup"){
    it("should arrive to pickup floor and then set destination floor"){
      ElevatorSystem(8, List(1, 3, 4, 8))
      ElevatorSystem.pickup(6, 2)
      val expectedCurrentFloors = List(1, 3, 6, 8)
      val expectedDestinations = List(None, None, Some(2), None)
      val expected = createTestElevators(expectedCurrentFloors, expectedDestinations)
      val actual = ElevatorSystem.getElevators
      assert(actual == expected)
    }
  }

  def createTestElevators(floors: List[Int], destinations: List[Option[Int]]): List[Elevator] = {
    floors.zip(destinations).zipWithIndex
      .map{case((currentFloor, destination), id) => Elevator(id, currentFloor, destination)}
  }
}
