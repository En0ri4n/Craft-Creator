package fr.eno.craftcreatorapi.item;

import fr.eno.craftcreatorapi.utils.Identifier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CCItem
{
    private Identifier id;
    private int maxStackSize;
}
