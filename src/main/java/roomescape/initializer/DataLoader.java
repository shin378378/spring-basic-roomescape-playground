package roomescape.initializer;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Profile("prod")
public class DataLoader implements ApplicationRunner {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        entityManager.createNativeQuery(
                "INSERT INTO member (name, email, password, role) " +
                        "VALUES ('어드민', 'admin@email.com', 'password', 'ADMIN'),\n" +
                        "('브라운', 'brown@email.com', 'password', 'USER')"
        ).executeUpdate();
    }
}

