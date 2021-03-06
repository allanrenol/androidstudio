package es.iessaladillo.pedrojoya.pr158;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

// Adaptador para la lista de alumnos.
public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.ViewHolder> {

    private final RealmResults<Alumno> mDatos;
    private final Realm mRealm;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    // Constructor.
    public AlumnosAdapter(Realm realm, RealmResults<Alumno> datos) {
        mRealm = realm;
        mDatos = datos;
        // Se establece que cada item tiene un id único.
        setHasStableIds(true);
    }

    // Cuando se debe crear una nueva vista para el elemento.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Se infla la especificación XML para obtener la vista-fila.
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false);
        // Se crea el contenedor de vistas para la fila.
        final ViewHolder viewHolder = new ViewHolder(itemView);

        // Se establecen los listener de la vista correspondiente al ítem
        // y de las subvistas.

        // Cuando se hace click sobre el elemento.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    // Se informa al listener.
                    onItemClickListener.onItemClick(v,
                            mDatos.get(viewHolder.getAdapterPosition()),
                            viewHolder.getAdapterPosition());
                }
            }
        });
        // Cuando se hace click largo sobre el elemento.
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    // Se informa al listener.
                    onItemLongClickListener.onItemLongClick(v,
                            mDatos.get(viewHolder.getAdapterPosition()),
                            viewHolder.getAdapterPosition());
                    return true;
                } else {
                    return false;
                }
            }
        });
        // Se retorna el contenedor.
        return viewHolder;
    }

    // Cuando se deben escribir los datos en las subvistas de la
    // vista correspondiente al ítem.
    @Override
    public void onBindViewHolder(AlumnosAdapter.ViewHolder holder, int position) {
        // Se obtiene el alumno correspondiente y se escriben sus datos
        // en las vistas.
        holder.bind(mDatos.get(position));
    }

    // Retorna el número de ítems gestionados.
    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    // Retorna el id único correspondiente al elemento de una posición.
    // Debemos sobrescribirlo para que se realicen las animaciones correctamente,
    // ya que al insertar o eliminar no especificamos posición insertada o eliminada
    // porque Realm lo gestiona automáticamente.
    // Además el RecyclerView debe tener rv.setStableIds(true).
    @Override
    public long getItemId(int position) {
        return mDatos.get(position).getTimestamp();
    }

    // Elimina un elemento de la lista.
    public void removeItem(int position) {
        // Se elimina el alumno de la base de datos.
        mRealm.beginTransaction();
        mDatos.deleteFromRealm(position);
        mRealm.commitTransaction();
        // Se notifica al adaptador la eliminación.
        notifyItemRemoved(position);
    }

    // Añade un elemento a la lista.
    @SuppressWarnings("unused")
    public void addItem(Alumno alumno) {
        // Se añade el alumno a la base de datos.
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(alumno);
        mRealm.commitTransaction();
        // Se indica al adaptador que han cambiado los datos (ojo NO se indoca
        // cual porque Realm no lo permite.
        notifyDataSetChanged();
    }

    // Establece el listener a informar cuando se hace click sobre un elemento de la lista.
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Establece el listener a informar cuando se hace click largo sobre un elemento de la lista.
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    // Contenedor de vistas para la vista-fila.
    @SuppressWarnings("unused")
    static class ViewHolder extends RecyclerView.ViewHolder {

        // El contenedor de vistas para un elemento de la lista debe contener...
        @BindView(R.id.lblNombre)
        public TextView lblNombre;
        @BindView(R.id.lblDireccion)
        public TextView lblDireccion;
        @BindView(R.id.imgFoto)
        public CircleImageView imgAvatar;
        @BindView(R.id.lblAsignaturas)
        public TextView lblAsignaturas;

        // El constructor recibe la vista-fila.
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        // Escribe el alumno en las vistas.
        public void bind(Alumno alumno) {
            // Se escriben los mDatos en la vista.
            lblNombre.setText(alumno.getNombre());
            lblDireccion.setText(alumno.getDireccion());
            RealmList<Asignatura> asignaturas = alumno.getAsignaturas();
            ArrayList<String> nombresAsignaturas = new ArrayList<>();
            for (Asignatura asignatura: asignaturas) {
                nombresAsignaturas.add(asignatura.getId());
            }
            lblAsignaturas.setText(getCadenaAsignaturas(nombresAsignaturas));
            String url = alumno.getUrlFoto();
            Glide.with(imgAvatar.getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(imgAvatar);
        }

        private String getCadenaAsignaturas(ArrayList<String> nombresAsignaturas) {
            if (nombresAsignaturas.size() > 0) {
                return TextUtils.join(", ", nombresAsignaturas);
            }
            else {
                return itemView.getContext().getString(R.string.ninguna);
            }
        }

    }

    // Interfaz que debe implementar el listener para cuando se haga click sobre un elemento.
    @SuppressWarnings("UnusedParameters")
    public interface OnItemClickListener {
        void onItemClick(View view, Alumno alumno, int position);
    }

    // Interfaz que debe implementar el listener para cuando se haga click largo sobre un elemento.
    @SuppressWarnings("UnusedParameters")
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, Alumno alumno, int position);
    }

}
