package com.gmk0232.whosthatpokemon.feature.quiz.data

import com.gmk0232.whosthatpokemon.feature.quiz.domain.CurrentRoundRepository
import com.gmk0232.whosthatpokemon.feature.quiz.domain.Pokemon
import com.gmk0232.whosthatpokemon.feature.quiz.domain.PokemonQuizRoundData

val pokemonNames = listOf(
    "Bulbasaur", "Ivysaur", "Venusaur", "Charmander", "Charmeleon", "Charizard",
    "Squirtle", "Wartortle", "Blastoise", "Caterpie", "Metapod", "Butterfree",
    "Weedle", "Kakuna", "Beedrill", "Pidgey", "Pidgeotto", "Pidgeot", "Rattata",
    "Raticate", "Spearow", "Fearow", "Ekans", "Arbok", "Pikachu", "Raichu",
    "Sandshrew", "Sandslash", "Nidoran♀", "Nidorina", "Nidoqueen", "Nidoran♂",
    "Nidorino", "Nidoking", "Clefairy", "Clefable", "Vulpix", "Ninetales",
    "Jigglypuff", "Wigglytuff", "Zubat", "Golbat", "Oddish", "Gloom", "Vileplume",
    "Paras", "Parasect", "Venonat", "Venomoth", "Diglett", "Dugtrio", "Meowth",
    "Persian", "Psyduck", "Golduck", "Mankey", "Primeape", "Growlithe", "Arcanine",
    "Poliwag", "Poliwhirl", "Poliwrath", "Abra", "Kadabra", "Alakazam",
    "Machop", "Machoke", "Machamp", "Bellsprout", "Weepinbell", "Victreebel",
    "Tentacool", "Tentacruel", "Geodude", "Graveler", "Golem", "Ponyta",
    "Rapidash", "Slowpoke", "Slowbro", "Magnemite", "Magneton", "Farfetch’d",
    "Doduo", "Dodrio", "Seel", "Dewgong", "Grimer", "Muk", "Shellder",
    "Cloyster", "Gastly", "Haunter", "Gengar", "Onix", "Drowzee", "Hypno",
    "Krabby", "Kingler", "Voltorb", "Electrode", "Exeggcute", "Exeggutor",
    "Cubone", "Marowak", "Hitmonlee", "Hitmonchan", "Lickitung", "Koffing",
    "Weezing", "Rhyhorn", "Rhydon", "Chansey", "Tangela", "Kangaskhan",
    "Horsea", "Seadra", "Goldeen", "Seaking", "Staryu", "Starmie", "Mr. Mime",
    "Scyther", "Jynx", "Electabuzz", "Magmar", "Pinsir", "Tauros", "Magikarp",
    "Gyarados", "Lapras", "Ditto", "Eevee", "Vaporeon", "Jolteon", "Flareon",
    "Porygon", "Omanyte", "Omastar", "Kabuto", "Kabutops", "Aerodactyl",
    "Snorlax", "Articuno", "Zapdos", "Moltres", "Dratini", "Dragonair",
    "Dragonite", "Mewtwo", "Mew"
)

class CurrentRoundRepositoryImpl(private val currentRoundDao: CurrentRoundDao) :
    CurrentRoundRepository {

    override suspend fun setCurrentRound(
        selectedPokemonNumber: Int,
        pokemonNumberOptions: List<Int>
    ) {

        val currentRoundEntity = CurrentRoundEntity(
            pokemonToGuess = selectedPokemonNumber,
            pokemonChoice1 = pokemonNumberOptions[1],
            pokemonChoice2 = pokemonNumberOptions[2],
            pokemonChoice3 = pokemonNumberOptions[3]
        )

        currentRoundDao.insertRound(currentRoundEntity)
    }

    override suspend fun getCurrentRound(): PokemonQuizRoundData {
        val currentRound = currentRoundDao.getCurrentRound()
        val pokemonToGuess = Pokemon(
            name = pokemonNames[currentRound.pokemonToGuess],
            number = currentRound.pokemonToGuess,
            imageUrl = ""
        )
        val pokemonChoice1 = Pokemon(
            name = pokemonNames[currentRound.pokemonChoice1],
            number = currentRound.pokemonChoice1,
            imageUrl = ""
        )
        val pokemonChoice2 = Pokemon(
            name = pokemonNames[currentRound.pokemonChoice2],
            number = currentRound.pokemonChoice2,
            imageUrl = ""
        )
        val pokemonChoice3 = Pokemon(
            name = pokemonNames[currentRound.pokemonChoice3],
            number = currentRound.pokemonChoice3,
            imageUrl = ""
        )

        return PokemonQuizRoundData(
            pokemonToGuess,
            listOf(pokemonToGuess, pokemonChoice1, pokemonChoice2, pokemonChoice3)
        )
    }
}