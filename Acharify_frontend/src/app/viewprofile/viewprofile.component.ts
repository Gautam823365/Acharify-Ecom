// import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms';
// import { UserProfile, UserProfileService } from '../service/userProfile.service';

// @Component({
//   selector: 'app-viewprofile',
//   templateUrl: './viewprofile.component.html',
//   styleUrls: ['./viewprofile.component.css'],
//   standalone: true,
//   imports: [CommonModule, FormsModule],
// })
// export class ViewprofileComponent implements OnInit {
//   @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

//   user: UserProfile | null = null;
//   isLoading = true;
//   error: string | null = null;

//   isEditing = false;
//   editableUser: UserProfile | null = null;

//   selectedFile: File | null = null;
//   timestamp = Date.now();

//   // ✅ define backend base URL
//   private readonly API_BASE_URL = 'http://localhost:8095';

//   constructor(private userProfileService: UserProfileService) {}

//   ngOnInit(): void {
//     this.loadUser();
//   }

//   loadUser() {
//     this.isLoading = true;
//     this.error = null;

//     this.userProfileService.getUserProfile().subscribe({
//       next: (data) => {
//         this.user = {
//           ...data,
//           address: data.address || { street: '', city: '', state: '', zip: '' },
//           // ✅ prepend backend URL if image path exists
//           profileImage: data.profileImage
//             ? this.API_BASE_URL + data.profileImage
//             : 'assets/default-avatar.png',
//         };
//         this.isLoading = false;
//       },
//       error: (err) => {
//         this.error = 'Failed to load user profile.';
//         this.isLoading = false;
//         console.error(err);
//       },
//     });
//   }

//   startEdit() {
//     if (this.user) {
//       this.editableUser = {
//         ...this.user,
//         address: this.user.address || { street: '', city: '', state: '', zip: '' },
//       };
//       this.isEditing = true;
//       this.selectedFile = null;
//     }
//   }

//   cancelEdit() {
//     this.isEditing = false;
//     this.editableUser = null;
//     this.selectedFile = null;
//   }

//   onAvatarClick() {
//     this.fileInput.nativeElement.click();
//   }

//   onFileSelected(event: any) {
//     const file: File = event.target.files[0];
//     if (file) {
//       this.selectedFile = file;
//     }
//   }

//   saveChanges() {
//     if (!this.editableUser) return;

//     this.isLoading = true;
//     this.error = null;

//     if (this.selectedFile) {
//       const formData = new FormData();
//       formData.append('file', this.selectedFile, this.selectedFile.name);

//       this.userProfileService.uploadProfileImage(formData).subscribe({
//         next: (res) => {
//           if (res.imageUrl) {
//             // ✅ always store full URL
//             this.editableUser!.profileImage = this.API_BASE_URL + res.imageUrl;
//           }
//           this.updateUserProfile(this.editableUser!);
//         },
//         error: (err) => {
//           this.error = 'Image upload failed.';
//           console.error(err);
//           this.isLoading = false;
//         },
//       });
//     } else {
//       this.updateUserProfile(this.editableUser);
//     }
//   }

//   private updateUserProfile(user: UserProfile) {
//     this.userProfileService.updateUserProfile(user).subscribe({
//       next: () => {
//         this.user = { ...user };
//         this.isEditing = false;
//         this.editableUser = null;
//         this.selectedFile = null;

//         this.timestamp = Date.now(); 
//         this.isLoading = false;
//         alert('Profile updated successfully');
//       },
//       error: (err) => {
//         this.isLoading = false;
//         this.error = 'Failed to update profile.';
//         console.error('Update error:', err);
//         alert(this.error);
//       },
//     });
//   }
// }

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserProfile, UserProfileService } from '../service/userProfile.service';

@Component({
  selector: 'app-viewprofile',
  templateUrl: './viewprofile.component.html',
  styleUrls: ['./viewprofile.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule],
})
export class ViewprofileComponent implements OnInit {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  user: UserProfile | null = null;
  isLoading = true;
  error: string | null = null;

  isEditing = false;
  editableUser: UserProfile | null = null;

  selectedFile: File | null = null;
  timestamp = Date.now();

  constructor(private userProfileService: UserProfileService) {}

  ngOnInit(): void {
    this.loadUser();
  }

  loadUser() {
    this.isLoading = true;
    this.error = null;

    this.userProfileService.getUserProfile().subscribe({
      next: (data) => {
        this.user = { ...data };
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Failed to load user profile.';
        this.isLoading = false;
        console.error(err);
      },
    });
  }

  startEdit() {
    if (this.user) {
      this.editableUser = {
        ...this.user,
        address: this.user.address || { street: '', city: '', state: '', zip: '' },
      };
      this.isEditing = true;
      this.selectedFile = null;
    }
  }

  cancelEdit() {
    this.isEditing = false;
    this.editableUser = null;
    this.selectedFile = null;
  }

  onAvatarClick() {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  saveChanges() {
    if (!this.editableUser) return;

    this.isLoading = true;
    this.error = null;

    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile, this.selectedFile.name);

      this.userProfileService.uploadProfileImage(formData).subscribe({
        next: (imageUrl) => {
          this.editableUser!.profileImage = imageUrl; // ✅ already full URL
          this.updateUserProfile(this.editableUser!);
        },
        error: (err) => {
          this.error = 'Image upload failed.';
          console.error(err);
          this.isLoading = false;
        },
      });
    } else {
      this.updateUserProfile(this.editableUser);
    }
  }

  private updateUserProfile(user: UserProfile) {
    this.userProfileService.updateUserProfile(user).subscribe({
      next: () => {
        this.user = { ...user };
        this.isEditing = false;
        this.editableUser = null;
        this.selectedFile = null;

        this.timestamp = Date.now(); // cache bust for <img>
        this.isLoading = false;
        alert('Profile updated successfully');
      },
      error: (err) => {
        this.isLoading = false;
        this.error = 'Failed to update profile.';
        console.error('Update error:', err);
        alert(this.error);
      },
    });
  }
}

