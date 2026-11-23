package com.example.navsamples.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

/**
 * Scene que dibuja dos entradas lado a lado (50 % / 50 %).
 * Es un data class para que Navigation3 pueda comparar escenas por igualdad.
 */
data class TwoPaneScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val firstEntry: NavEntry<T>,
    val secondEntry: NavEntry<T>,
) : Scene<T> {

    override val entries: List<NavEntry<T>> = listOf(firstEntry, secondEntry)

    override val content: @Composable () -> Unit = {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) { firstEntry.Content() }
            Box(modifier = Modifier.weight(1f)) { secondEntry.Content() }
        }
    }

    companion object {
        internal const val TWO_PANE_KEY = "TwoPane"

        /** Metadata para marcar una entrada como candidata a mostrarse en dos paneles. */
        fun twoPane(): Map<String, Any> = mapOf(TWO_PANE_KEY to true)
    }
}

/**
 * En Navigation3 1.1+ `calculateScene` ya NO es @Composable, así que el ancho de
 * pantalla se calcula fuera (ver [rememberTwoPaneSceneStrategy]) y se pasa aquí.
 */
class TwoPaneSceneStrategy<T : Any>(
    private val isWideEnough: Boolean,
) : SceneStrategy<T> {

    override fun SceneStrategyScope<T>.calculateScene(
        entries: List<NavEntry<T>>,
    ): Scene<T>? {
        if (!isWideEnough) return null

        val lastTwo = entries.takeLast(2)
        if (lastTwo.size < 2 || !lastTwo.all { it.metadata.containsKey(TwoPaneScene.TWO_PANE_KEY) }) {
            return null
        }

        val first = lastTwo.first()
        val second = lastTwo.last()
        return TwoPaneScene(
            key = first.contentKey to second.contentKey,
            previousEntries = entries.dropLast(1),
            firstEntry = first,
            secondEntry = second,
        )
    }
}

private const val MEDIUM_WIDTH_DP = 600

@Composable
fun <T : Any> rememberTwoPaneSceneStrategy(): TwoPaneSceneStrategy<T> {
    val wide = LocalConfiguration.current.screenWidthDp >= MEDIUM_WIDTH_DP
    return remember(wide) { TwoPaneSceneStrategy(wide) }
}
