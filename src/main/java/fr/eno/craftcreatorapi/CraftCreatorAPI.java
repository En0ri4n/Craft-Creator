package fr.eno.craftcreatorapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CraftCreatorAPI
{
    @Getter
    private static final CraftCreatorAPI instance = new CraftCreatorAPI();
    private static boolean initialized = false;

    @Getter
    private static final String kubeJsModId = "kubejs";

    private SupportedModsBase supportedModsBase;

    private final List<Object> required = Arrays.asList(supportedModsBase);

    public void initialize() throws CraftCreatorException
    {
        if(initialized)
            throw new CraftCreatorException("CraftCreatorAPI has already been initialized !");

        initialized = true;
        for(Object obj : this.required)
            if(Objects.isNull(obj))
                throw new CraftCreatorException(obj.getClass().getName() + " has not been initialized !");
    }
}
