package com.example.nguyenkhoahung.changephonenumberprefix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ContactDTO> {
    private Context context;
    private List<ContactDTO> listContact;
    private ContactDTO contact;
    private DataDTO data;
    private List<DataDTO> listPhoneNumber;

    private LayoutInflater mInflater;
    private boolean mNotifyOnChange = true;

    private static class ViewHolder {
        CheckBox cbxSelectNumber;
        TextView tvName;
        TextView tvNumber;
        TextView tvNumberType;
        int pos; //to store the position of the item within the list
    }

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
    public ContactDTO getItem(int position) {
        return listContact.get(position);
    }

    @Override
    public int getPosition(@Nullable ContactDTO item) {
        return super.getPosition(item);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        contact = listContact.get(position);
        listPhoneNumber = contact.getPhoneList();
        holder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_contact_item, parent, false);
        }
        holder.cbxSelectNumber = convertView.findViewById(R.id.cbxSelectedPhoneNumber);
        holder.cbxSelectNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listContact.get(position).setSelected(buttonView.isChecked());
            }
        });
        holder.tvName = convertView.findViewById(R.id.tvName);
        holder.tvNumber = convertView.findViewById(R.id.tvPhoneNumber);
        holder.tvNumberType = convertView.findViewById(R.id.tvPhoneNumberType);
        holder.cbxSelectNumber.setChecked(contact.isSelected());
        try {
            if (!Common.isEmptyString(contact.getDisplayName())) {
                holder.tvName.setText(contact.getDisplayName());
            }
            if(!Common.isEmptyString(contact.getPhoneNumber())){
                holder.tvNumberType.setText(contact.getPhoneNumberType());
                holder.tvNumber.setText(Common.validateNumberPhone(contact.getPhoneNumber()));
            }
        } catch (Exception e) {
            Log.e("ListAdapter",e.toString());
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }



}

