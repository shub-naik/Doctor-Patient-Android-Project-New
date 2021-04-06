package patient

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shubham.doctorpatientandroidappnew.R

class ContactsAdapter(private val mContacts: List<Contact>) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {
    private val TAG = "RECYCLER_VIEW"
    private var counter = 0

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val nameTextView: TextView = listItemView.findViewById(R.id.contact_name)
        val messageButton: Button = listItemView.findViewById(R.id.message_button)
    }


    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        Log.e(TAG, "onCreateViewHolder - ${++counter}")
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_contact, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ContactsAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        Log.e(TAG, "onBindViewHolder - ${++counter}")

        val contact: Contact = mContacts[position]
        // Set item views based on your views and data model
        val textView = viewHolder.nameTextView
        textView.text = contact.name
        val button = viewHolder.messageButton
        button.text = if (contact.isOnline) "Message" else "Offline"
        button.isEnabled = contact.isOnline
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mContacts.size
    }
}