import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vialidolid.R
import com.example.vialidolid.RAlumbradoP


class ReportAPAAdapter(private val mContext: Context, private val listaReportes: List<RAlumbradoP>)
    : RecyclerView.Adapter<ReportAPAAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.activity_fragmento_reportes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reporte = listaReportes[position]

        holder.itemView.findViewById<TextView>(R.id.txtFalla).text = reporte.descripcion
        holder.itemView.findViewById<TextView>(R.id.txtEstado).text = reporte.estatus
        holder.itemView.findViewById<TextView>(R.id.txtFecha).text = reporte.fecha
        holder.itemView.findViewById<TextView>(R.id.txtUbicacion).text = reporte.ubicacion
    }

    override fun getItemCount(): Int {
        return listaReportes.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}