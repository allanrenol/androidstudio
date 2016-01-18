package es.iessaladillo.pedrojoya.pr083;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.ViewHolder> {

    // Interfaz que debe implementar el listener para cuando se haga click
    // sobre un elemento.
    public interface OnItemClickListener {

        void onItemClick(View view, Alumno alumno, int position);
    }
    // Interfaz que debe implementar el listener para cuando se haga click
    // largo sobre un elemento.
    public interface OnItemLongClickListener {

        void onItemLongClick(View view, Alumno alumno, int position);
    }
    // Interfaz que debe implementar el listener para cuando la lista pase a
    // o deje de estar vacía.
    public interface OnEmptyStateChangedListener {

        void onEmptyStateChanged(boolean isEmpty);
    }

    private ArrayList<Alumno> mDatos;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnEmptyStateChangedListener mOnEmptyStateChangedListener;
    private final SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private boolean mIsEmpty = true;

    // Constructor.
    public AlumnosAdapter(ArrayList<Alumno> datos) {
        mDatos = datos;
        checkEmptyStateChanged();
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
                if (mOnItemClickListener != null) {
                    // Se informa al listener.
                    mOnItemClickListener.onItemClick(v,
                            mDatos.get(viewHolder.getAdapterPosition()),
                            viewHolder.getAdapterPosition());
                }
            }
        });
        // Cuando se hace click largo sobre el elemento.
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    // Se informa al listener.
                    mOnItemLongClickListener.onItemLongClick(v,
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
        // Se obtiene el alumno correspondiente.
        Alumno alumno = mDatos.get(position);
        // Se escriben los mDatos en la vista.
        holder.onBind(alumno);
    }

    // Retorna el número de ítems gestionados.
    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    // Retorna los datos.
    public ArrayList<Alumno> getData() {
        return mDatos;
    }

    public void swapData(ArrayList<Alumno> alumnos) {
        mDatos = alumnos;
        checkEmptyStateChanged();
        notifyDataSetChanged();
    }

    // Retorna un objeto de la colección.
    public Alumno getItem(int position) {
        return mDatos.get(position);
    }


    // Elimina un elemento de la lista.
    public void removeItem(int position) {
        mDatos.remove(position);
        notifyItemRemoved(position);
        // Se comprueba si pasa a estar vacía.
        checkEmptyStateChanged();
    }

    // Añade un elemento a la lista.
    public void addItem(Alumno alumno, int position) {
        // Se añade el elemento.
        mDatos.add(position, alumno);
        // Se notifica que se ha insertado un elemento en la última posición.
        notifyItemInserted(position);
        // Si comprueba si deja de estar vacía.
        checkEmptyStateChanged();
    }

    // Comprueba si ha pasa a estar vacía o deja de estar vacía.
    private void checkEmptyStateChanged() {
        // Deja de estar vacía
        if (mIsEmpty && mDatos.size() > 0) {
            mIsEmpty = false;
            if (mOnEmptyStateChangedListener != null) {
                mOnEmptyStateChangedListener.onEmptyStateChanged(false);
            }
        }
        else if (!mIsEmpty && mDatos.size()==0) {
            mIsEmpty = true;
            if (mOnEmptyStateChangedListener != null) {
                mOnEmptyStateChangedListener.onEmptyStateChanged(true);
            }
        }
    }

    // Retorna si la lista está vacía.
    public boolean isEmpty() {
        return mIsEmpty;
    }

    // Establece el listener a informar cuando se hace click sobre un
    // elemento de la lista.
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    // Establece el listener a informar cuando se hace click largo sobre un
    // elemento de la lista.
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    // Establece el listener a informar cuando la lista pasa a o deja de
    // estar vacía.
    public void setOnEmptyStateChangedListener(
            OnEmptyStateChangedListener listener) {
        mOnEmptyStateChangedListener = listener;
        checkEmptyStateChanged();
    }

    // Contenedor de vistas para la vista-fila.
    static class ViewHolder extends RecyclerView.ViewHolder {
        // El contenedor de vistas para un elemento de la lista debe contener...
        private final ImageView imgFoto;
        private final TextView lblNombre;
        private final TextView lblCurso;
        private final TextView lblEdad;
        private final TextView lblRepetidor;

        // El constructor recibe la vista-fila.
        public ViewHolder(View itemView) {
            super(itemView);
            // Se obtienen las vistas de la vista-fila.
            imgFoto = (ImageView) itemView
                    .findViewById(R.id.imgFoto);
            lblNombre = (TextView) itemView
                    .findViewById(R.id.lblNombre);
            lblCurso = (TextView) itemView
                    .findViewById(R.id.lblCurso);
            lblEdad = (TextView) itemView
                    .findViewById(R.id.lblEdad);
            lblRepetidor = (TextView) itemView
                    .findViewById(R.id.lblRepetidor);
        }

        // Escribe los datos del alumno en las vistas.
        public void onBind(Alumno alumno) {
            //holder.imgFoto.setImageResource(alumno.getFoto());
            lblNombre.setText(alumno.getNombre());
            lblCurso.setText(alumno.getCurso());
            lblEdad.setText(lblEdad.getContext().getString(R.string.anios, alumno.getEdad()));
            Picasso.with(imgFoto.getContext()).load(alumno.getFoto()).into(imgFoto);
            // El fondo del TextView con la edad es diferente si es menor de
            // edad.
            if (alumno.getEdad() < 18) {
                lblEdad.setTextColor(lblEdad.getContext().getResources().getColor(R.color.accent));
            } else {
                lblEdad.setTextColor(lblEdad.getContext().getResources().getColor(R.color.primary_text));
            }
            // Si el alumno es repetidor se muestra el TextView correspondiente.
            if (alumno.isRepetidor()) {
                lblRepetidor.setVisibility(View.VISIBLE);
            } else {
                lblRepetidor.setVisibility(View.INVISIBLE);
            }
        }


    }

}

