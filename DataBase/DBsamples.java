package DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Hat;
import Model.Kick;
import Model.Sample;
import Model.Snare;

public class DBsamples {
  private static Connection connection;
  private static Statement statement;
  private static ResultSet resultSet;

  public static void connectionDB() throws ClassNotFoundException, SQLException {
    connection = null;
    Class.forName("org.postgresql.Driver");
    connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/daw_samples",
        "mihailvasilev",
        "095147");
    System.out.println("База данных подключена!");
  }

  public static void createTables() throws SQLException {
    statement = connection.createStatement();

    statement.execute(
        "CREATE TABLE IF NOT EXISTS kicks (" +
            "id SERIAL PRIMARY KEY, " +
            "name VARCHAR(100) NOT NULL, " +
            "length_ms INT, " +
            "volume DOUBLE PRECISION, " +
            "low_freq INT, " +
            "high_freq INT, " +
            "bass_level INT" +
            ");");
    System.out.println("Таблица kicks существует");

    statement.execute(
        "CREATE TABLE IF NOT EXISTS snares (" +
            "id SERIAL PRIMARY KEY, " +
            "name VARCHAR(100) NOT NULL, " +
            "length_ms INT, " +
            "volume DOUBLE PRECISION, " +
            "low_freq INT, " +
            "high_freq INT, " +
            "resonance INT, " +
            "punch INT" +
            ");");
    System.out.println("Таблица snares существует");

    statement.execute(
        "CREATE TABLE IF NOT EXISTS hats (" +
            "id SERIAL PRIMARY KEY, " +
            "name VARCHAR(100) NOT NULL, " +
            "length_ms INT, " +
            "volume DOUBLE PRECISION, " +
            "low_freq INT, " +
            "high_freq INT, " +
            "tail_length INT, " +
            "is_closed BOOLEAN" +
            ");");
    System.out.println("Таблица hats существует");
  }

  public static void insertKick(Kick kick) throws SQLException {
    String sql = "INSERT INTO kicks (name, length_ms, volume, low_freq, high_freq, bass_level) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, kick.getName());
      pstmt.setInt(2, kick.getLengthMs());
      pstmt.setDouble(3, kick.getVolume());
      pstmt.setInt(4, kick.getLowFrequency());
      pstmt.setInt(5, kick.getHighFrequency());
      pstmt.setInt(6, kick.getBassLevel());
      pstmt.executeUpdate();
      System.out.println("Kick '" + kick.getName() + "' добавлен в таблицу kicks");
    }
  }

  public static void insertSnare(Snare snare) throws SQLException {
    String sql = "INSERT INTO snares (name, length_ms, volume, low_freq, high_freq, resonance, punch) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, snare.getName());
      pstmt.setInt(2, snare.getLengthMs());
      pstmt.setDouble(3, snare.getVolume());
      pstmt.setInt(4, snare.getLowFrequency());
      pstmt.setInt(5, snare.getHighFrequency());
      pstmt.setInt(6, snare.getResonance());
      pstmt.setInt(7, snare.getPunch());
      pstmt.executeUpdate();
      System.out.println("Snare '" + snare.getName() + "' добавлен в таблицу snares");
    }
  }

  public static void insertHat(Hat hat) throws SQLException {
    String sql = "INSERT INTO hats (name, length_ms, volume, low_freq, high_freq, tail_length, is_closed) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setString(1, hat.getName());
      pstmt.setInt(2, hat.getLengthMs());
      pstmt.setDouble(3, hat.getVolume());
      pstmt.setInt(4, hat.getLowFrequency());
      pstmt.setInt(5, hat.getHighFrequency());
      pstmt.setInt(6, hat.getTailLength());
      pstmt.setBoolean(7, hat.isClosed());
      pstmt.executeUpdate();
      System.out.println("Hat '" + hat.getName() + "' добавлен в таблицу hats");
    }
  }

  public static void insertSample(Sample sample) throws SQLException {
    if (sample instanceof Kick) {
      insertKick((Kick) sample);
    } else if (sample instanceof Snare) {
      insertSnare((Snare) sample);
    } else {
      insertHat((Hat) sample);
    }
  }

  public static List<Sample> loadAllSamples() throws SQLException {
    List<Sample> samples = new ArrayList<>();
    statement = connection.createStatement();

    resultSet = statement.executeQuery("SELECT * FROM kicks");
    while (resultSet.next()) {
      Kick kick = new Kick(
          resultSet.getString("name"),
          resultSet.getInt("length_ms"),
          resultSet.getDouble("volume"),
          resultSet.getInt("low_freq"),
          resultSet.getInt("high_freq"),
          resultSet.getInt("bass_level"));
      samples.add(kick);
    }

    resultSet = statement.executeQuery("SELECT * FROM snares");
    while (resultSet.next()) {
      Snare snare = new Snare(
          resultSet.getString("name"),
          resultSet.getInt("length_ms"),
          resultSet.getDouble("volume"),
          resultSet.getInt("low_freq"),
          resultSet.getInt("high_freq"),
          resultSet.getInt("resonance"),
          resultSet.getInt("punch"));
      samples.add(snare);
    }

    resultSet = statement.executeQuery("SELECT * FROM hats");
    while (resultSet.next()) {
      Hat hat = new Hat(
          resultSet.getString("name"),
          resultSet.getInt("length_ms"),
          resultSet.getDouble("volume"),
          resultSet.getInt("low_freq"),
          resultSet.getInt("high_freq"),
          resultSet.getInt("tail_length"),
          resultSet.getBoolean("is_closed"));
      samples.add(hat);
    }

    System.out.println("Всего загружено сэмплов: " + samples.size());
    return samples;
  }

  public static void deleteKickById(int id) throws SQLException {
    String sql = "DELETE FROM kicks WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      int rows = pstmt.executeUpdate();
      System.out.println(rows > 0 ? "Kick удалён." : "Kick не найден.");
    }
  }

  public static void deleteSnareById(int id) throws SQLException {
    String sql = "DELETE FROM snares WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      int rows = pstmt.executeUpdate();
      System.out.println(rows > 0 ? "Snare удалён." : "Snare не найден.");
    }
  }

  public static void deleteHatById(int id) throws SQLException {
    String sql = "DELETE FROM hats WHERE id = ?";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
      pstmt.setInt(1, id);
      int rows = pstmt.executeUpdate();
      System.out.println(rows > 0 ? "Hat удалён." : "Hat не найден.");
    }
  }

  public static void writeDefaultSamples() throws SQLException {
    statement = connection.createStatement();

    statement.execute("INSERT INTO kicks (name, length_ms, volume, low_freq, high_freq, bass_level) " +
        "VALUES ('Deep Kick', 250, 0.85, 60, 4000, 70);");
    statement.execute("INSERT INTO kicks (name, length_ms, volume, low_freq, high_freq, bass_level) " +
        "VALUES ('Punchy Kick', 200, 0.9, 50, 3500, 85);");

    statement.execute("INSERT INTO snares (name, length_ms, volume, low_freq, high_freq, resonance, punch) " +
        "VALUES ('Crack Snare', 180, 0.9, 200, 8000, 65, 80);");
    statement.execute("INSERT INTO snares (name, length_ms, volume, low_freq, high_freq, resonance, punch) " +
        "VALUES ('Tight Snare', 150, 0.8, 150, 9000, 50, 90);");

    statement.execute("INSERT INTO hats (name, length_ms, volume, low_freq, high_freq, tail_length, is_closed) " +
        "VALUES ('Closed Hat', 120, 0.75, 5000, 15000, 40, true);");
    statement.execute("INSERT INTO hats (name, length_ms, volume, low_freq, high_freq, tail_length, is_closed) " +
        "VALUES ('Open Hat', 300, 0.7, 6000, 16000, 70, false);");

    System.out.println("Таблицы kicks, snares, hats заполнены тестовыми данными.");
  }

  public static void closeDB() throws SQLException {
    if (resultSet != null)
      resultSet.close();
    if (statement != null)
      statement.close();
    if (connection != null)
      connection.close();
    System.out.println("Соединения с БД закрыты.");
  }
}