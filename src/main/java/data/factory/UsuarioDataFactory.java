package data.factory;

import model.Usuario;
import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Random;

public class UsuarioDataFactory {
    private static Faker faker = new Faker(new Locale("PT-BR"));

    private UsuarioDataFactory() {}

    public static Usuario usuarioValido() {
        return novoUsuario();
    }

    public static Usuario usuarioComDadosAusente() {
        Usuario usuarioSemDados = novoUsuario();
        usuarioSemDados.setNome(StringUtils.EMPTY);
        usuarioSemDados.setEmail(StringUtils.EMPTY);
        usuarioSemDados.setPassword(StringUtils.EMPTY);
        usuarioSemDados.setAdministrador(StringUtils.EMPTY);

        return usuarioSemDados;
    }

    private static Usuario novoUsuario() {

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(faker.name().firstName() + " " + faker.name().lastName());
        novoUsuario.setEmail(faker.internet().emailAddress());
        novoUsuario.setPassword(faker.internet().password());
        novoUsuario.setAdministrador("true");

        return novoUsuario;
    }

}