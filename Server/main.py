from flask import Flask, request, jsonify
import firebase_admin
from firebase_admin import credentials, db
import pandas as pd
from sklearn.preprocessing import StandardScaler
import joblib

# 🚨 Firebase kimlik bilgilerini burada kendi dosyanızla değiştirin!
# cred = credentials.Certificate("SİZİN_FIREBASE_JSON_DOSYANIZ.json")
cred = credentials.Certificate("YOUR_FIREBASE_CREDENTIALS.json")

# 🚨 Firebase veritabanı URL'nizi burada kendi projenizle değiştirin!
# firebase_admin.initialize_app(cred, {'databaseURL': 'SİZİN_DATABASE_URL'})
firebase_admin.initialize_app(cred, {'databaseURL': 'YOUR_FIREBASE_DATABASE_URL'})

ref = db.reference('pulse_data')

# 🚨 Model dosyalarınızı kendi oluşturduğunuz dosyalarla değiştirin!
# scaler = joblib.load('SİZİN_SCALER.pkl')
# loaded_model = joblib.load('SİZİN_MODEL.pkl')
scaler = joblib.load('YOUR_SCALER.pkl')
loaded_model = joblib.load('YOUR_MODEL.pkl')

app = Flask(__name__)

@app.route('/save_pulse', methods=['POST'])
def save_pulse():
    try:
        data = request.json
        pulse_rate = data.get('pulse_rate')
        timestamp = data.get('timestamp')

        if not pulse_rate or not timestamp:
            return jsonify(error="Missing pulse_rate or timestamp"), 400

        print("Gelen veri:", data)

        user_pulse_ref = ref.push({
            "pulse_rate": pulse_rate,
            "timestamp": timestamp
        })

        # 🚨 Kullanıcıdan yaş ve cinsiyet bilgisi almıyorsanız, burada manuel değerler kullanıyorsunuz!
        # Bunu değiştirmek için istemciden (mobil uygulama gibi) bu bilgileri de alabilirsiniz.
        new_data = pd.DataFrame({
            'Age': [30],   # Varsayılan yaş (DEĞİŞTİRİLEBİLİR)
            'Gender': [1], # Varsayılan cinsiyet (1: Erkek, 0: Kadın)
            'HeartRate': [pulse_rate]
        })

        scaled_new_data = scaler.transform(new_data)
        prediction = loaded_model.predict(scaled_new_data)
        result = int(prediction[0])

        ref.child(user_pulse_ref.key).update({"result": result})

        return jsonify(result="Pulse data saved successfully", prediction=result)
    except Exception as e:
        print("Hata:", e)
        return jsonify(error=f'Error: {str(e)}')

@app.route('/get_pulse', methods=['GET'])
def get_pulse():
    try:
        pulse_data = ref.get()
        if not pulse_data:
            return jsonify(error="No data found"), 404

        return jsonify(pulse_data)
    except Exception as e:
        print("Hata:", e)
        return jsonify(error=f'Error: {str(e)}')

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)
