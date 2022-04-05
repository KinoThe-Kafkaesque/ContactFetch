package two.one.contactfetch;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import two.one.contactfetch.entities.Contact;
import two.one.contactfetch.web.ContactController;
import androidx.hilt.lifecycle.ViewModelInject;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
    public class ContactViewModel extends ViewModel {
        private final ContactController contactController;

        @ViewModelInject
        public ContactViewModel(ContactController contactController) {
            this.contactController = contactController;
        }

        private MutableLiveData<List<Contact>> liveData = new MutableLiveData<>();

//        public LiveData<List<Contact>> getLiveData() {
//            if (liveData == null)
//                liveData = new MutableLiveData<>();
//            return liveData;
//        }
//
//        public void getContact() {
//            contactController.getAll()
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(contactData ->
//                                    liveData.setValue(contactData)
//                            , Throwable::printStackTrace);
//        }
//        public void getContactByPhoneNumber(String telephone) {
//            contactController.getByNumber(telephone)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(contactData ->
//                                    liveData.setValue(contactData)
//                            , Throwable::printStackTrace);
//        }
//
//        public void AddContact(Contact contact) {
//            contactController.createUser(contact)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(response ->
//                                    Log.d("TAG", "AddContact: "+response.getNom())
//                            , Throwable::printStackTrace);
//        }
}
