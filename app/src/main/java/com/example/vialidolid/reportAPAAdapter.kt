import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vialidolid.R
import com.example.vialidolid.databinding.ActivityFragmentoReportesBinding
import com.example.vialidolid.rAlumbradoP

class reportAPAAdapter(private val mContext: Context, private val listaReportes: List<rAlumbradoP>)
    : RecyclerView.Adapter<reportAPAAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_fragmento_reportes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reporte = listaReportes[position]

        holder.binding.txtFalla.text = reporte.descripcion
        holder.binding.txtEstado.text = reporte.estatus
        holder.binding.txtFecha.text = reporte.fecha
        holder.binding.txtUbicacion.text = reporte.ubicacion
    }

    override fun getItemCount(): Int {
        return listaReportes.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ActivityFragmentoReportesBinding.bind(itemView)
    }
}