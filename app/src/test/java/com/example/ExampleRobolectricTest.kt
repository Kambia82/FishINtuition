package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FishINtuition", appName)
  }

  @Test
  fun `fishing intelligence engine generates valid tactical plans`() {
    val engine = com.example.data.engine.FishingIntelligenceEngine()
    val species = com.example.data.model.SpeciesCatalog.allSpecies.first()
    val water = com.example.data.model.WaterCatalog.presetWaters.first()
    val conditions = com.example.data.model.EnvironmentalConditions()

    val recommendation = engine.generateRecommendation(
      species = species,
      waterBody = water,
      conditions = conditions,
      userTackleBox = emptyList()
    )

    assertEquals(3, recommendation.tacticalPlans.size)
    assertEquals("A", recommendation.tacticalPlans[0].planLetter)
    assertEquals("B", recommendation.tacticalPlans[1].planLetter)
    assertEquals("C", recommendation.tacticalPlans[2].planLetter)
  }
}
