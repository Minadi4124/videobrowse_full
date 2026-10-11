package com.nsoft.sliit.videobrowse.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseMigration {
    @Bean
    public ApplicationRunner addMissingVideoColumns(JdbcTemplate jdbcTemplate){
        return args -> {
            try { jdbcTemplate.execute("ALTER TABLE videos ADD COLUMN IF NOT EXISTS video_type VARCHAR(31)"); } catch (Exception ignored) {}
            try { jdbcTemplate.execute("ALTER TABLE videos ADD COLUMN IF NOT EXISTS visibility VARCHAR(31)"); } catch (Exception ignored) {}
            try { jdbcTemplate.execute("ALTER TABLE videos ADD COLUMN IF NOT EXISTS subject VARCHAR(255)"); } catch (Exception ignored) {}
            try { jdbcTemplate.execute("ALTER TABLE videos ADD COLUMN IF NOT EXISTS rating VARCHAR(255)"); } catch (Exception ignored) {}
            try { jdbcTemplate.execute("ALTER TABLE videos ADD COLUMN IF NOT EXISTS category_ref_id BIGINT"); } catch (Exception ignored) {}
            try { jdbcTemplate.execute("UPDATE videos SET video_type='BASE' WHERE video_type IS NULL"); } catch (Exception ignored) {}
            try { jdbcTemplate.execute("UPDATE videos SET visibility='PUBLIC' WHERE visibility IS NULL"); } catch (Exception ignored) {}
        };
    }
}


