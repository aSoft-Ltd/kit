package kit.composables

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.layout.height
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Spacer
import com.jakewharton.mosaic.ui.Text
import kit.service.KitCommand

@Composable
internal fun Usage(title: String) = Column {
    Text(title)
    Spacer(Modifier.height(1))
    Text("Available Commands are")
    KitCommand.entries.forEach {
        Text("  ${it::class.simpleName?.lowercase()}")
    }
}