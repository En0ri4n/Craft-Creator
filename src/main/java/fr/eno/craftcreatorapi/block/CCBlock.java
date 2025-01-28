package fr.eno.craftcreatorapi.block;

import fr.eno.craftcreatorapi.utils.CCShape;
import fr.eno.craftcreatorapi.utils.Identifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CCBlock
{
    private Identifier id;
    private CCShape shape;
    private boolean isFullCube;
}
