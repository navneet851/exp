import SwiftUI

@main
struct iOSApp: App {

    init(){
        requestNotificationPermission()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

// func requestNotificationPermission() {
//     UNUserNotificationCenter
//         .current()
//         .requestAuthorization(options: [.alert, .badge, .sound]) { (success, error) in
//             if success {
//                 print("Permission granted.")
//             } else if let error = error {
//                 print(error.localizedDescription)
//             }
//         }
// }

func requestNotificationPermission() {
    UNUserNotificationCenter.current().getNotificationSettings { settings in
        switch settings.authorizationStatus {
        case .notDetermined:
            UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
                if granted {
                    print("Permission granted.")
                } else {
                    print("Permission denied.")
                }
            }
        case .denied:
            DispatchQueue.main.async {
                let alert = UIAlertController(
                    title: "Permission Needed",
                    message: "Please enable notifications in Settings.",
                    preferredStyle: .alert
                )
                alert.addAction(UIAlertAction(title: "Go to Settings", style: .default) { _ in
                    if let url = URL(string: UIApplication.openSettingsURLString) {
                        UIApplication.shared.open(url)
                    }
                })
                alert.addAction(UIAlertAction(title: "Cancel", style: .cancel))
                UIApplication.shared.windows.first?.rootViewController?.present(alert, animated: true)
            }
        default:
            break
        }
    }
}