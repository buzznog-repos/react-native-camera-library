using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Camera.Library.RNCameraLibrary
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNCameraLibraryModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNCameraLibraryModule"/>.
        /// </summary>
        internal RNCameraLibraryModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNCameraLibrary";
            }
        }
    }
}
