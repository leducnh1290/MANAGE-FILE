public void openDirectory() {
        String path = Environment.getExternalStorageDirectory() + "/Android/data/com.bumbum.sg246/files";
                                                                      // ghi đường dẫn của app đó vào
        File file = new File(path);
        String startDir="", finalDirPath;

        if (file.exists()) {
            startDir = "Android%2Fdata%2Fcom.bumbum.sg246%2Ffiles";
          // ghi đường dẫn của app đó vào
        }

        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

        Intent intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();


        Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

        String scheme = uri.toString();

        Log.d("TAG", "INITIAL_URI scheme: " + scheme);

        scheme = scheme.replace("/root/", "/document/");

        finalDirPath = scheme + "%3A" + startDir;

        uri = Uri.parse(finalDirPath);

        intent.putExtra("android.provider.extra.INITIAL_URI", uri);

        Log.d("TAG", "uri: " + uri.toString());

        try {
            startActivityForResult(intent, 6);
        } catch (ActivityNotFoundException ignored) {

        }}
//hàm này có thể gọi khi cần truy cập vào file data app khác
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode==6) {
            if (data != null) {
                Uri uri = data.getData();
                        DocumentFile documentFile = DocumentFile.fromTreeUri(MainActivity.this,uri);
                        documentFile.createDirectory("data");
                    // these are my SharedPerfernce values for remembering the path
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(uri,takeFlags);
                    sharedPreferences.edit().putString("uri", uri.toString()+"%2Fdata").commit();
              // lưu lại đường dẫn có gì còn dùng tiếp
            }
        }
    }
//Cách dùng
 DocumentFile fromTreeUri = DocumentFile.fromTreeUri(context, Uri.parse(url));
  DocumentFile file[] = fromTreeUri.listFiles();
     DocumentFile file[] = fromTreeUri.listFiles();
        for(DocumentFile file_name:file)
            if(file_name.exists()) {
                Log.e("name", file_name.getName());
                file_name.delete();
            }
//copy file các thứ
 private String copyFile(String inputPath, String inputFile, Uri treeUri) {
    InputStream in = null;
    OutputStream out = null;
    String error = null;
    DocumentFile pickedDir = DocumentFile.fromTreeUri(getActivity(), treeUri);
   //tree Uri là cái uri bác lưu trong SharedPreferences
    String extension = inputFile.substring(inputFile.lastIndexOf(".")+1,inputFile.length());

    try {
        DocumentFile newFile = pickedDir.createFile("audio/"+extension, inputFile);
        out = getActivity().getContentResolver().openOutputStream(newFile.getUri());
        in = new FileInputStream(inputPath + inputFile);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();
        // write the output file (You have now copied the file)
        out.flush();
        out.close();

    } catch (FileNotFoundException fnfe1) {
        error = fnfe1.getMessage();
    } catch (Exception e) {
        error = e.getMessage();
    }
    return error;
}
