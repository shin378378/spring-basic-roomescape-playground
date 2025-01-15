package roomescape.initializer;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("test")
public class TestDataLoader implements ApplicationRunner {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        loadTestData();
    }

    private void loadTestData() {
        entityManager.createNativeQuery(
                "INSERT INTO member (name, email, password, role) VALUES " +
                        "('어드민', 'admin@email.com', 'password', 'ADMIN'),\n" +
                        "('브라운', 'brown@email.com', 'password', 'USER')"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT INTO theme (name, description) VALUES " +
                        "('테마1', '테마1입니다.'), ('테마2', '테마2입니다.'), ('테마3', '테마3입니다.')"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT INTO time (time_value) VALUES ('10:00'), ('12:00'), ('14:00'), ('16:00'), ('18:00'), ('20:00')"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT INTO reservation (member_id, name, date, time_id, theme_id) VALUES \n" +
                        "(1, '어드민', '2024-03-01', 1, 1), \n" +
                        "(1, '어드민', '2024-03-01', 2, 2), \n" +
                        "(1, '어드민', '2024-03-01', 3, 3)"
        ).executeUpdate();

        entityManager.createNativeQuery(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('브라운', '2024-03-01', 1, 2)"
        ).executeUpdate();
    }
}
