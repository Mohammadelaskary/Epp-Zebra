package net.gbs.epp_project.Ui.ContainersReceiving.AddNewTruck

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.getEditTextText
import net.gbs.epp_project.databinding.AddNewTrailerDialogLayoutBinding

class AddNewTrailerDialog(private val context: Context,val onAddTrailerButtonClicked: OnAddTrailerButtonClicked):Dialog(context) {
    private lateinit var binding: AddNewTrailerDialogLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNewTrailerDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Tools.clearInputLayoutError(binding.containerNo,binding.trailerNo)
        binding.addContainer.setOnClickListener {
            val containerNo = Tools.getEditTextText(binding.containerNo)
            if (containerNo.isNotEmpty()){
                binding.containers.text =
                     if(binding.containers.text.toString().isEmpty()) containerNo else "${binding.containers.text}, $containerNo"
                binding.containerNo.editText?.setText("")
            }else{
                binding.containerNo.error =
                    context.getString(R.string.please_enter_container_number)
            }
        }
        binding.addTrailer.setOnClickListener {
            val trailerNo = getEditTextText(binding.trailerNo)
            val containers = binding.containers.text.toString()
            if (trailerNo.isNotEmpty()){
                if (containers.isNotEmpty()){
                    onAddTrailerButtonClicked.onTrailerDataAdded(trailerNo,containers)
                } else {
                    binding.containerNo.error =
                        context.getString(R.string.please_enter_container_number)
                }
            } else {
                binding.trailerNo.error =
                    context.getString(R.string.please_enter_trailer_number)
            }
        }
    }
    interface OnAddTrailerButtonClicked{
        fun onTrailerDataAdded(trailerNo:String, containers:String)
    }
}