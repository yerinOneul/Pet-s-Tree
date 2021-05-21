package kr.ac.gachon.sw.petstree.certreq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import kr.ac.gachon.sw.petstree.R;
import kr.ac.gachon.sw.petstree.model.CertRequest;
import kr.ac.gachon.sw.petstree.util.Firestore;

public class ExpectReqFragment extends Fragment implements CertRequestAdapter.OnCertRequestItemClickListener {
    private String LOG_TAG = this.getClass().getSimpleName();
    private View view;
    private ArrayList<CertRequest> certRequestList;
    private RecyclerView rvExpectReq;
    private CertRequestAdapter certRequestAdapter;
    private ListenerRegistration reqDataListener = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_expectreq, container, false);

        rvExpectReq = view.findViewById(R.id.rv_expectreq);
        rvExpectReq.setHasFixedSize(true);
        rvExpectReq.setLayoutManager(new LinearLayoutManager(getActivity()));

        certRequestList = new ArrayList<>();
        certRequestAdapter = new CertRequestAdapter(certRequestList, this);
        rvExpectReq.setAdapter(certRequestAdapter);

        getReqData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(reqDataListener != null) {
            reqDataListener.remove();
        }
    }

    /**
     * 인증 요청 데이터를 실시간으로 받아온다
     */
    private void getReqData() {
        reqDataListener = Firestore.getExpectReqData()
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.w(LOG_TAG, "Event Error", error);
                            Toast.makeText(getContext(), R.string.error_server, Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }

                        if(value != null) {
                            if(value.getDocuments().size() == 0) {
                                Toast.makeText(getActivity(), R.string.certreq_empty, Toast.LENGTH_SHORT).show();
                            }
                            // 변경된 리스트 가져옴
                            List<DocumentChange> changeList = value.getDocumentChanges();

                            // 모든 List 가져와서 Adapter에 등록
                            for(DocumentChange change : changeList) {
                                CertRequest certRequest = change.getDocument().toObject(CertRequest.class);
                                certRequest.setDocId(change.getDocument().getId());

                                if(change.getType() == DocumentChange.Type.ADDED) {
                                    certRequestAdapter.addItem(certRequest);
                                }
                                else if(change.getType() == DocumentChange.Type.REMOVED) {
                                    certRequestAdapter.removeItem(certRequest);
                                }
                            }
                            certRequestAdapter.notifyDataSetChanged();
                        }
                        else {
                            Log.w(LOG_TAG, "Snapshot Data NULL!");
                        }
                    }
                });
    }

    @Override
    public void onClick(View v, int pos) {
        Intent certReqView = new Intent(getActivity(), CertRequestViewActivity.class);
        certReqView.putExtra("certrequest", certRequestList.get(pos));
        startActivity(certReqView);
    }
}
