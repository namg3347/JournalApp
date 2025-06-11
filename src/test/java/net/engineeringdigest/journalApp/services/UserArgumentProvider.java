package net.engineeringdigest.journalApp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import net.engineeringdigest.journalApp.entities.User;

public class UserArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext arg0) throws Exception {
        List<String> list = new ArrayList<>();
        list.add("USER");
        list.add("ADMIN");
       return Stream.of(
            Arguments.of(User.builder().userName("ram").password("ram").roles(list).build()),
            Arguments.of(User.builder().userName("piyush").password("piyush").roles(new ArrayList<String>()).build())
       );
    }  
}
