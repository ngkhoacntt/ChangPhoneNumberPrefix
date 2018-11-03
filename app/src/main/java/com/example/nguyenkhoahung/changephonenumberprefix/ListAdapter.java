package com.example.nguyenkhoahung.changephonenumberprefix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<DataDTO> {
    private Context context;
    private List<ContactDTO> listContact;
    private ContactDTO contact;
    private DataDTO data;
    private List<DataDTO> listPhoneNumber;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    public ListAdapter(Context context, int listViewItem, List<ContactDTO> listContact) {
        super(context, listViewItem);
        this.context = context;
        this.listContact = listContact;
    }

    public List<ContactDTO> getListContact() {
        return listContact;
    }

    public void setListContact(List<ContactDTO> listContact) {
        this.listContact = listContact;
    }

    @Override
    public int getCount() {
        return listContact.size();
    }

    @Nullable
    @Override
    public DataDTO getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable DataDTO item) {
        return super.getPosition(item);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        int type = getItemViewType(position);
        contact = listContact.get(position);
        listPhoneNumber = contact.getPhoneList();
        holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_contact_item, parent, false);
        }
        holder.cbxSelectNumber = convertView.findViewById(R.id.cbxSelectedPhoneNumber);
        holder.tvName = convertView.findViewById(R.id.tvName);
        holder.tvNumber = convertView.findViewById(R.id.tvPhoneNumber);
        holder.tvNumberType = convertView.findViewById(R.id.tvPhoneNumberType);
        holder.cbxSelectNumber.setChecked(true);
        holder.tvName.setText(contact.getDisplayName());
        holder.tvNumberType.setText(listPhoneNumber.get(0).getDataType());
        holder.tvNumber.setText(listPhoneNumber.get(0).getDataValue());
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    private static class ViewHolder {
        CheckBox cbxSelectNumber;
        TextView tvName;
        TextView tvNumber;
        TextView tvNumberType;
        int pos; //to store the position of the item within the list
    }
}

