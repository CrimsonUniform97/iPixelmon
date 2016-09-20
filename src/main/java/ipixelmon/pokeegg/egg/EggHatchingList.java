package ipixelmon.pokeegg.egg;

import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import java.util.*;

public class EggHatchingList
{

    public static final EggHatchingList instance = new EggHatchingList();

    private Map<EnumPokemon, Integer> pokemonMap;

    private EggHatchingList()
    {
        pokemonMap = new HashMap<>();

        pokemonMap.put(EnumPokemon.Bulbasaur, 2);
        pokemonMap.put(EnumPokemon.Charmander, 2);
        pokemonMap.put(EnumPokemon.Squirtle, 2);
        pokemonMap.put(EnumPokemon.Caterpie, 2);
        pokemonMap.put(EnumPokemon.Weedle, 2);
        pokemonMap.put(EnumPokemon.Pidgey, 2);
        pokemonMap.put(EnumPokemon.Rattata, 2);
        pokemonMap.put(EnumPokemon.Spearow, 2);
        pokemonMap.put(EnumPokemon.Pikachu, 2);
        pokemonMap.put(EnumPokemon.Clefairy, 2);
        pokemonMap.put(EnumPokemon.Jigglypuff, 2);
        pokemonMap.put(EnumPokemon.Zubat, 2);
        pokemonMap.put(EnumPokemon.Geodude, 2);
        pokemonMap.put(EnumPokemon.Magikarp, 2);

        pokemonMap.put(EnumPokemon.Ekans, 5);
        pokemonMap.put(EnumPokemon.Sandshrew, 5);
        pokemonMap.put(EnumPokemon.Nidoranfemale, 5);
        pokemonMap.put(EnumPokemon.Nidoranmale, 5);
        pokemonMap.put(EnumPokemon.Vulpix, 5);
        pokemonMap.put(EnumPokemon.Oddish, 5);
        pokemonMap.put(EnumPokemon.Paras, 5);
        pokemonMap.put(EnumPokemon.Venonat, 5);
        pokemonMap.put(EnumPokemon.Meowth, 5);
        pokemonMap.put(EnumPokemon.Psyduck, 5);
        pokemonMap.put(EnumPokemon.Mankey, 5);
        pokemonMap.put(EnumPokemon.Growlithe, 5);
        pokemonMap.put(EnumPokemon.Poliwag, 5);
        pokemonMap.put(EnumPokemon.Abra, 5);
        pokemonMap.put(EnumPokemon.Machop, 5);
        pokemonMap.put(EnumPokemon.Farfetchd, 5);
        pokemonMap.put(EnumPokemon.Bellsprout, 5);
        pokemonMap.put(EnumPokemon.Tentacool, 5);
        pokemonMap.put(EnumPokemon.Ponyta, 5);
        pokemonMap.put(EnumPokemon.Slowpoke, 5);
        pokemonMap.put(EnumPokemon.Magnemite, 5);
        pokemonMap.put(EnumPokemon.Doduo, 5);
        pokemonMap.put(EnumPokemon.Seel, 5);
        pokemonMap.put(EnumPokemon.Grimer, 5);
        pokemonMap.put(EnumPokemon.Shellder, 5);
        pokemonMap.put(EnumPokemon.Gastly, 5);
        pokemonMap.put(EnumPokemon.Drowzee, 5);
        pokemonMap.put(EnumPokemon.Krabby, 5);
        pokemonMap.put(EnumPokemon.Voltorb, 5);
        pokemonMap.put(EnumPokemon.Exeggcute, 5);
        pokemonMap.put(EnumPokemon.Cubone, 5);
        pokemonMap.put(EnumPokemon.Lickitung, 5);
        pokemonMap.put(EnumPokemon.Koffing, 5);
        pokemonMap.put(EnumPokemon.Rhyhorn, 5);
        pokemonMap.put(EnumPokemon.Tangela, 5);
        pokemonMap.put(EnumPokemon.Kangaskhan, 5);
        pokemonMap.put(EnumPokemon.Horsea, 5);
        pokemonMap.put(EnumPokemon.Goldeen, 5);
        pokemonMap.put(EnumPokemon.Staryu, 5);
        pokemonMap.put(EnumPokemon.Tauros, 5);
        pokemonMap.put(EnumPokemon.Porygon, 5);

        pokemonMap.put(EnumPokemon.Onix, 10);
        pokemonMap.put(EnumPokemon.Hitmonlee, 10);
        pokemonMap.put(EnumPokemon.Hitmonchan, 10);
        pokemonMap.put(EnumPokemon.Chansey, 10);
        pokemonMap.put(EnumPokemon.Scyther, 10);
        pokemonMap.put(EnumPokemon.Jynx, 10);
        pokemonMap.put(EnumPokemon.Electabuzz, 10);
        pokemonMap.put(EnumPokemon.Magmar, 10);
        pokemonMap.put(EnumPokemon.Pinsir, 10);
        pokemonMap.put(EnumPokemon.Lapras, 10);
        pokemonMap.put(EnumPokemon.Eevee, 10);
        pokemonMap.put(EnumPokemon.Omanyte, 10);
        pokemonMap.put(EnumPokemon.Kabuto, 10);
        pokemonMap.put(EnumPokemon.Aerodactyl, 10);
        pokemonMap.put(EnumPokemon.Snorlax, 10);
        pokemonMap.put(EnumPokemon.Dratini, 10);
        pokemonMap.put(EnumPokemon.MrMime, 10);
    }

    public EnumPokemon getRandomPokemon(int km)
    {
        if (km != 2 && km != 5 && km != 10)
        {
            return null;
        }

        List<EnumPokemon> options = new ArrayList<>();
        for (EnumPokemon pokemon : pokemonMap.keySet())
        {
            if (pokemonMap.get(pokemon) == km)
            {
                options.add(pokemon);
            }
        }

        return options.get(new Random().nextInt(options.size() - 1));
    }


}
