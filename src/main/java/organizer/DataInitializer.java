package organizer;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import organizer.model.entities.Account;
import organizer.model.entities.TechnicalRequirement;
import organizer.model.entities.Units;
import organizer.model.repositories.*;

import java.sql.SQLException;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Autowired
    private TechnicalRequirementRepository technicalRequirementRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UnitsRepository unitsRepository;


    @Bean
    public CommandLineRunner getCommandLineRunner() {
        return args -> {
//            accountRepository.deleteAll();
//
//            Account admin = new Account();
//            admin.setLoginId("RamzinAK");
//            admin.setName("Рамзин Андрей Константинович");
//            admin.setRole(Account.Role.Admin);
//            admin.setPassword("000999");
//
//            accountRepository.save(admin);
//
//            technicalRequirementRepository.save(new TechnicalRequirement("", "ПОТРЕБНОСТЬ БЕЗ ОЛ", "", "", "", "000000000", LocalDate.now()));
//
//            unitsRepository.save(new Units("ШТ"));
//            unitsRepository.save(new Units("М"));
//            unitsRepository.save(new Units("Т"));
//            unitsRepository.save(new Units("КМП"));

        };
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseaServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }


}
