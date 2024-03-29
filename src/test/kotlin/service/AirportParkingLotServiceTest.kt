package service

import config.Spots
import config.VehicleType
import exception.SpotNotAvailableException
import exception.VehicleNotParkedException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import repository.AirportParkingLot
import java.time.LocalDateTime

class AirportParkingLotServiceTest {
    @Test
    fun `it should return ticket with spot for parking Motorcycle and Scooter`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        val ticket = airportParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.MOTORCYCLE_SCOOTER, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Car and SUV`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        val ticket = airportParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.CAR_SUV, ticket.vehicleType)
    }

    @Test
    fun `it should return ticket with spot for parking Bus and Truck`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        val ticket = airportParkingLotService.park(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticket.ticketNumber)
        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(VehicleType.BUS_TRUCK, ticket.vehicleType)
    }

    @Test
    fun `it should return receipt with fees for un-parking Motorcycle and Scooter`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())
        airportParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)

        val receipt = airportParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(40, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Car and SUV`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())
        airportParkingLotService.park(VehicleType.CAR_SUV)

        val receipt = airportParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(60, receipt.fees)
    }

    @Test
    fun `it should return receipt with fees for un-parking Bus and Truck`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())
        airportParkingLotService.park(VehicleType.BUS_TRUCK)

        val receipt = airportParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertEquals(1, receipt.receiptNumber)
        Assertions.assertEquals(0, receipt.fees)
    }

    @Test
    fun `it should start spot numbers from one for every vehicle type`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        val ticketMotorcycleScooter = airportParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
        val ticketCarSUV = airportParkingLotService.park(VehicleType.CAR_SUV)
        val ticketBusTruck = airportParkingLotService.park(VehicleType.BUS_TRUCK)

        Assertions.assertEquals(1, ticketMotorcycleScooter.spotNumber)
        Assertions.assertEquals(1, ticketCarSUV.spotNumber)
        Assertions.assertEquals(1, ticketBusTruck.spotNumber)
    }

    @Test
    fun `it should increment spot number for every vehicle with same vehicle type`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        val ticketCarSUVFirst = airportParkingLotService.park(VehicleType.CAR_SUV)
        val ticketCarSUVSecond = airportParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticketCarSUVFirst.spotNumber)
        Assertions.assertEquals(2, ticketCarSUVSecond.spotNumber)
    }

    @Test
    fun `it should throw an exception when un-parking a vehicle which is not parked`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        Assertions.assertThrows(VehicleNotParkedException::class.java) {
            airportParkingLotService.unpark(2, LocalDateTime.now().plusHours(2))
        }
    }

    @Test
    fun `it should throw an exception when un-parking a parked vehicle twice`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.CAR_SUV)
        airportParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))

        Assertions.assertThrows(VehicleNotParkedException::class.java) {
            airportParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Motorcycle and Scooter is exceeded`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        Assertions.assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.motorcycleScooter + 1) {
                airportParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
            }
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Car and SUV is exceeded`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        Assertions.assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.carSUV + 1) {
                airportParkingLotService.park(VehicleType.CAR_SUV)
            }
        }
    }

    @Test
    fun `it should throw an exception when parking limit of Bus and Truck is exceeded`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        Assertions.assertThrows(SpotNotAvailableException::class.java) {
            for (spot in 0 until Spots.busTruck + 1) {
                airportParkingLotService.park(VehicleType.BUS_TRUCK)
            }
        }
    }

    @Test
    fun `it should park two vehicles and un-park first vehicle and park another vehicle`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.CAR_SUV)
        airportParkingLotService.park(VehicleType.CAR_SUV)
        airportParkingLotService.unpark(1, LocalDateTime.now().plusHours(2))
        val ticket = airportParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(1, ticket.spotNumber)
        Assertions.assertEquals(3, ticket.ticketNumber)
    }

    @Test
    fun `it should park two vehicles and un-park second vehicle and park another vehicle`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.CAR_SUV)
        airportParkingLotService.park(VehicleType.CAR_SUV)
        airportParkingLotService.unpark(2, LocalDateTime.now().plusHours(2))
        val ticket = airportParkingLotService.park(VehicleType.CAR_SUV)

        Assertions.assertEquals(2, ticket.spotNumber)
        Assertions.assertEquals(3, ticket.ticketNumber)
    }

    @Test
    fun `Motorcycle parked for 55 mins`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
        val receipt = airportParkingLotService.unpark(
            1,
            LocalDateTime.now().plusMinutes(55)
        )

        Assertions.assertEquals(0, receipt.fees)
    }

    @Test
    fun `Motorcycle parked for 14 hours and 59 mins`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
        val receipt = airportParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(14).plusMinutes(59)
        )

        Assertions.assertEquals(60, receipt.fees)
    }

    @Test
    fun `Motorcycle parked for 1 day and 12 hours`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.MOTORCYCLE_SCOOTER)
        val receipt = airportParkingLotService.unpark(
            1,
            LocalDateTime.now().plusDays(1).plusHours(12)
        )

        Assertions.assertEquals(160, receipt.fees)
    }

    @Test
    fun `Car parked for 50 mins`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.CAR_SUV)
        val receipt = airportParkingLotService.unpark(
            1,
            LocalDateTime.now().plusMinutes(50)
        )

        Assertions.assertEquals(60, receipt.fees)
    }

    @Test
    fun `SUV parked for 23 hours and 59 mins`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.CAR_SUV)
        val receipt = airportParkingLotService.unpark(
            1,
            LocalDateTime.now().plusHours(23).plusMinutes(59)
        )

        Assertions.assertEquals(80, receipt.fees)
    }

    @Test
    fun `Car parked for 3 days and 1 hour`() {
        val airportParkingLotService = AirportParkingLotService(AirportParkingLot())

        airportParkingLotService.park(VehicleType.CAR_SUV)
        val receipt = airportParkingLotService.unpark(
            1,
            LocalDateTime.now().plusDays(3).plusHours(1)
        )

        Assertions.assertEquals(400, receipt.fees)
    }
}