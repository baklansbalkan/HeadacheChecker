package org.baklansbalkan.HeadacheChecker.config;

import org.baklansbalkan.HeadacheChecker.dto.HeadacheDTO;
import org.baklansbalkan.HeadacheChecker.dto.UserDTO;
import org.baklansbalkan.HeadacheChecker.models.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        Converter<Set<Role>, Set<String>> rolesConverter = ctx ->
                ctx.getSource() != null ?
                        ctx.getSource().stream()
                                .map(role -> role.getName().name())
                                .collect(Collectors.toSet()) :
                        Collections.emptySet();

        modelMapper.typeMap(User.class, UserDTO.class)
                .addMappings(mapper -> {
                    mapper.using(rolesConverter)
                            .map(User::getRoles, UserDTO::setRoles);
                });

        Converter<Localisation, String> localisationConverter = ctx ->
                ctx.getSource() != null ? ctx.getSource().name() : null;

        Converter<TimesOfDay, String> timesOfDayConverter = ctx ->
                ctx.getSource() != null ? ctx.getSource().name() : null;

        modelMapper.typeMap(Headache.class, HeadacheDTO.class)
                .addMappings(mapper -> {
                    mapper.using(localisationConverter)
                            .map(Headache::getLocalisation, HeadacheDTO::setLocalisation);
                    mapper.using(timesOfDayConverter)
                            .map(Headache::getTimesOfDay, HeadacheDTO::setTimesOfDay);
                });

        return modelMapper;
    }
}
