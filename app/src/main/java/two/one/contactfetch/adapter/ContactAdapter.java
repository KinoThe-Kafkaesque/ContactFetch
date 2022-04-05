package two.one.contactfetch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import two.one.contactfetch.R;
import two.one.contactfetch.entities.Contact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> contactList;
    private Context context;

    public ContactAdapter(Context context,List<Contact> contactList){
        this.context = context;
        this.contactList = contactList;
    }
    public ContactAdapter(Context context) {
        this.context = context;
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        TextView contactPhone;

        ContactViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhone = itemView.findViewById(R.id.contactPhone);

        }
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        holder.contactName.setText(contactList.get(position).getNom());
        holder.contactPhone.setText(contactList.get(position).getTelephone());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
    public void setList(List<Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }
}
