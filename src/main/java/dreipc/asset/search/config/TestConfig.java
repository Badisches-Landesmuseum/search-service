package dreipc.asset.search.config;

import dreipc.asset.search.services.ColorSeeder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("local")
@Configuration
public class TestConfig {

    private final ColorSeeder seeder;

    public TestConfig(ColorSeeder seeder) {
        this.seeder = seeder;
    }

    @PostConstruct
    private void seed(){
        this.seeder.seed();
    }
}
