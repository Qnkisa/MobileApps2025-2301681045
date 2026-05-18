package com.example.zerog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.zerog.data.local.GravityLog
import com.example.zerog.databinding.FragmentCalculatorBinding
import com.example.zerog.ui.viewmodel.GravityViewModel

class CalculatorFragment : Fragment() {

    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GravityViewModel by activityViewModels()

    /** Holds the last successful calculation so Save/Share can use it. */
    private var lastResult: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        binding.btnCalculate.setOnClickListener { performCalculation() }
        binding.btnSave.setOnClickListener { saveToLog() }
        binding.btnShare.setOnClickListener { shareResult() }
    }

    private fun setupSlider() {
        binding.tvPercentage.text = "${binding.sliderGravity.value.toInt()}%"
        binding.sliderGravity.addOnChangeListener { _, value, _ ->
            binding.tvPercentage.text = "${value.toInt()}%"
        }
    }

    private fun performCalculation() {
        val name = binding.etItemName.text.toString().trim()
        val weightStr = binding.etEarthWeight.text.toString().trim()

        var valid = true
        if (name.isEmpty()) {
            binding.tilItemName.error = "Enter an item name"
            valid = false
        } else binding.tilItemName.error = null

        if (weightStr.isEmpty()) {
            binding.tilEarthWeight.error = "Enter the Earth weight"
            valid = false
        } else binding.tilEarthWeight.error = null

        if (!valid) return

        val earthWeight = weightStr.toDoubleOrNull()
        if (earthWeight == null || earthWeight <= 0) {
            binding.tilEarthWeight.error = "Enter a valid positive number"
            return
        }
        binding.tilEarthWeight.error = null

        val reduction = binding.sliderGravity.value.toInt()

        // Formula: Weight_Zero-G = Weight_Earth × (1 − Reduction/100)
        lastResult = earthWeight * (1.0 - reduction / 100.0)

        binding.tvResult.text = "${"%.4f".format(lastResult)} kg"
        binding.tvFormula.text = "$earthWeight × (1 − $reduction/100) = ${"%.4f".format(lastResult)} kg"
        binding.cardResult.visibility = View.VISIBLE
        binding.btnSave.isEnabled = true
        binding.btnShare.isEnabled = true
    }

    private fun saveToLog() {
        val name = binding.etItemName.text.toString().trim()
        val earthWeight = binding.etEarthWeight.text.toString().toDoubleOrNull() ?: return
        val reduction = binding.sliderGravity.value.toInt()
        val result = lastResult ?: return

        viewModel.insert(
            GravityLog(
                itemName = name,
                earthWeight = earthWeight,
                gravityReduction = reduction,
                calculatedWeight = result
            )
        )
        Toast.makeText(requireContext(), "\"$name\" saved to log!", Toast.LENGTH_SHORT).show()
    }

    private fun shareResult() {
        val name = binding.etItemName.text.toString().trim()
        val earthWeight = binding.etEarthWeight.text.toString()
        val reduction = binding.sliderGravity.value.toInt()
        val result = lastResult ?: return

        val text = "🚀 Zero-G Weight Calculation\n\n" +
                "Item: $name\n" +
                "Earth Weight: $earthWeight kg\n" +
                "Anti-Gravity Reduction: $reduction%\n" +
                "Zero-G Weight: ${"%.4f".format(result)} kg\n\n" +
                "Calculated with Zero-G Weight Simulator"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            putExtra(Intent.EXTRA_SUBJECT, "Zero-G Calculation: $name")
        }
        startActivity(Intent.createChooser(intent, "Share with Crew"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
