import java.sql.*;
import java.util.*;

class DeliveryBoy {
    int id;
    int freeAt;

    DeliveryBoy(int id) {
        this.id = id;
        this.freeAt = 0;
    }
}

class Order {
    int id;
    int orderTime;
    int travelTime;

    Order(int id, int orderTime, int travelTime) {
        this.id = id;
        this.orderTime = orderTime;
        this.travelTime = travelTime;
    }
}

public class FoodDeliverySystemDB {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/FoodDelivery?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total customers (N): ");
        int N = sc.nextInt();
        System.out.print("Enter total drivers (M): ");
        int M = sc.nextInt();

        List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            System.out.println("Enter details for Customer " + i + ":");
            int orderTime = sc.nextInt();
            int travelTime = sc.nextInt();
            orders.add(new Order(i, orderTime, travelTime));
        }

        List<DeliveryBoy> drivers = new ArrayList<>();
        for (int i = 1; i <= M; i++) {
            drivers.add(new DeliveryBoy(i));
        }

        Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        Statement st = conn.createStatement();

       
        st.executeUpdate("DELETE FROM Assignments");
        st.executeUpdate("DELETE FROM Orders");
        st.executeUpdate("DELETE FROM Drivers");

       
        for (DeliveryBoy d : drivers) {
            String sql = "INSERT INTO Drivers(driver_id, free_at) VALUES(" + d.id + ",0)";
            st.executeUpdate(sql);
        }

     
        for (Order o : orders) {
            String sql = "INSERT INTO Orders(order_id, order_time, travel_time) VALUES(" + o.id + "," + o.orderTime + "," + o.travelTime + ")";
            st.executeUpdate(sql);
        }

        for (Order o : orders) {
            int assignedDriverId = -1;
            ResultSet rs = st.executeQuery("SELECT driver_id, free_at FROM Drivers ORDER BY driver_id ASC");
            while (rs.next()) {
                int driverId = rs.getInt("driver_id");
                int freeAt = rs.getInt("free_at");
                if (freeAt <= o.orderTime) {
                    assignedDriverId = driverId;
                    String updateSql = "UPDATE Drivers SET free_at=" + (o.orderTime + o.travelTime) + " WHERE driver_id=" + driverId;
                    st.executeUpdate(updateSql);
                    break;
                }
            }

            if (assignedDriverId == -1) {
                System.out.println("C" + o.id + " - No Food :-(");
                String sql = "INSERT INTO Assignments(order_id, driver_id, status) VALUES(" + o.id + ", NULL, 'No Food')";
                st.executeUpdate(sql);
            } else {
                System.out.println("C" + o.id + " - D" + assignedDriverId);
                String sql = "INSERT INTO Assignments(order_id, driver_id, status) VALUES(" + o.id + "," + assignedDriverId + ",'Delivered')";
                st.executeUpdate(sql);
            }
        }

        System.out.println("\nData stored in MySQL successfully!");
    }
}
