package de.salocin.ui

import de.salocin.Fonts.create
import de.salocin.util.condition
import de.salocin.util.map
import javafx.geometry.Pos
import org.controlsfx.glyphfont.FontAwesome
import tornadofx.*

class PackageDetailsView : View() {

    override val root = titledpane("Details") {
        isCollapsible = false
        vbox(spacing = 10.0) {
            label("Name")
            text(UiContext.selectedPackageItemProperty.map { it?.value }.map { it?.name })

            label("Installation Path")
            listview(UiContext.selectedPackageInstallLocationProperty)

            hbox(spacing = 10.0) {
                alignment = Pos.BASELINE_RIGHT

                button("", FontAwesome.Glyph.DOWNLOAD.create()) {
                    disableProperty().bind(UiContext.selectedPackageProperty.condition {
                        it?.downloadable?.not() ?: true
                    })
                }
            }
        }

        UiContext.selectedPackageItemProperty.addListener { _, _, newValue ->
            newValue?.value?.refreshInstallLocationAsync()
        }
    }
}
