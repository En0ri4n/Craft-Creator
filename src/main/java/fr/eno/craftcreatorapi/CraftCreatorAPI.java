package fr.eno.craftcreatorapi;

import fr.eno.craftcreatorapi.init.InitBlockBase;
import fr.eno.craftcreatorapi.init.InitItemBase;
import fr.eno.craftcreatorapi.utils.ComponentHelperBase;
import fr.eno.craftcreatorapi.utils.CraftCreatorException;
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

    private ReferenceBase references;
    private SupportedModsBase supportedModsBase;
    private InitBlockBase initBlockBase;
    private InitItemBase initItemBase;
    private RecipeCreatorBase<?, ?> recipeCreatorBase;
    private ComponentHelperBase<?> componentHelperBase;

    private final List<Object> required = Arrays.asList(references, supportedModsBase, initBlockBase, initItemBase);

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
