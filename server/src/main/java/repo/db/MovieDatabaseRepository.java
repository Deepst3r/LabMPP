package repo.db;

import domain.Movie;
import domain.Validator.Validator;
import domain.Validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import repo.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class MovieDatabaseRepository implements Repository<UUID, Movie> {
    @Autowired
    private JdbcOperations jdbcOperations;

    private Validator<Movie> validator;

    public MovieDatabaseRepository(Validator<Movie> validator) {
        this.validator = validator;
    }


//    @Override
//    public Optional<Movie> findOne(UUID uuid) {
//        Movie movie = null;
//        String sql = "select * from Movies" +
//                " where id=?";
//
//        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
//                PASSWORD)) {
//
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, uuid.toString());
//            ResultSet resultSet = statement.executeQuery();
//
//            if (resultSet.next()) {
//                UUID id = UUID.fromString(resultSet.getString("id"));
//                String title = resultSet.getString("title");
//                double rating = resultSet.getDouble("rating");
//                int year = resultSet.getInt("year");
//                String genre = resultSet.getString("genre");
//
//                movie = new Movie(title, rating, year, genre);
//                movie.setId(id);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return Optional.ofNullable(movie);
//    }

    @Override
    public List<Movie> findAll() {
        String sql = "select * from Movies";

        return jdbcOperations.query(sql, (rs, rowNum) -> {
            UUID id = UUID.fromString(rs.getString("id"));
            String title = rs.getString("title");
            double rating = rs.getDouble("rating");
            int year = rs.getInt("year");
            String genre = rs.getString("genre");

            Movie movie = new Movie(title, rating, year, genre);
            movie.setId(id);
            return movie;
        });
    }

    @Override
    public Optional<Movie> save(Movie entity) throws ValidatorException {
        validator.validate(entity);

        String sql = "insert into movies(id,title, rating, year, genre) " +
                "values (?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getId().toString());
            statement.setString(2, entity.getTitle());
            statement.setDouble(3, entity.getRating());
            statement.setInt(4, entity.getYear());
            statement.setString(5, entity.getGenre());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Boolean> delete(UUID uuid) {
        String sql = "delete from movies " +
                "where id=?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, uuid.toString());

            if (statement.executeUpdate() > 0) {
                return Optional.of(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(false);
    }

    @Override
    public Optional<Movie> update(Movie entity) throws ValidatorException {
        String sql = "update movie set title=?, rating=?, year=?, genre=? where id=?";
        try (Connection connection = DriverManager.getConnection(URL, USERNAME,
                PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getTitle());
            statement.setDouble(2, entity.getRating());
            statement.setInt(3, entity.getYear());
            statement.setString(4, entity.getGenre());
            statement.setString(5, entity.getId().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        return Optional.of(entity);
    }
}
